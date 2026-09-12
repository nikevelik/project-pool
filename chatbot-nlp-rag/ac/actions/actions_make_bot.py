from .classes import Environment, RazgaRUI, Helper, RazgaRUI_factory

import requests
import json

from typing import Any, Text, Dict, List

from rasa_sdk import Tracker, FormValidationAction, Action
from rasa_sdk.executor import CollectingDispatcher
from rasa_sdk.events import EventType, SlotSet
from rasa_sdk.types import DomainDict

# Slot names
SLOT_BOT_NAME = "SL_bot_name"
SLOT_XCONN_NAME = "SL_xconn_name"
SLOT_XINSTRUMENT_NAME = "SL_xinstrument_name"
SLOT_START_BOT_FLAG = "SL_start_bot_flag"
SLOT_ABORT_MAKE_BOT_FLAG = "SL_abort_make_bot_flag"
SLOT_USER_ID = "user_id"
SLOT_ENV = "env"
# Intent names
INTENT_ABORT = "INT_abort"
INTENT_WANTS_NEW_BOT = "INT_wants_new_bot"
# Error messages
ERROR_API_ACCESS = "Could not access the API for making bot. Please try again later. Making Bot had been cancelled."
ERROR_NO_CONNECTOR = "You do not have a connector named \"{}\". Try again."
ERROR_NO_INSTRUMENT = "The exchange instrument specified ({}) was not found. Try again."
ERROR_BOT_CREATION = "The API could not make your bot! Please try again later. Making Bot had been cancelled."
URL_EXCHANGE_CONNECTORS = "exchange_connectors.php"
ERROR_NO_CONNECTORS = f"You do not have any Exchange Connectors. Please create one first from the [Exchange Connectors page]({URL_EXCHANGE_CONNECTORS}). Once you are done, come back and type in 'new bot' to proceed"
# Success messages
URL_BOT_PROFILE = "bot_profile.php"
SUCCESS_BOT_CREATED = f"The bot has been created! You can go to [Bot Profile]({URL_BOT_PROFILE}?bid={{}}) to check it" 
SUCCESS_BOT_RUNNING = f"Bot is now running! You can go to [Bot Profile]({URL_BOT_PROFILE}?bid={{}}) to monitor how it works" 
BUTTON_YES = "yes"
BUTTON_NO = "no"
# Button payloads
PAYLOAD_AFFIRM = "/affirm"
PAYLOAD_DENY = "/deny"
INTENT_AFFFIRM = "affirm"
INTENT_DENY = "deny"
CHOOSE_CONNECTOR_MESSAGE = "Choose Exchange Connector for the bot:"
START_BOT_PROMPT = "Would you like to start the bot?"
ABORT_CONFIRMATION_PROMPT = "Are you sure you want to abort?"
ERROR_BOT_START_API = "Could not access the API for starting bot. Please try again later. Starting the Bot had been cancelled."
URL_EXCHANGE_INSTRUMENTS_LST = "exchange_instruments_list.php"
CHOOSE_INSTRUMENT_PROMPT = f"Choose Exchange Instrument (You can find list of all available instruments [here]({URL_EXCHANGE_INSTRUMENTS_LST})) for the bot (or type it in):"
MAKING_BOT_CANCELLED_MESSAGE = "Making Bot had been cancelled."
BOT_COULD_NOT_START_CRASH = "Bot could not start. Starting the Bot had been cancelled. Details:"
DIDNT_GET_THAT_MESSAGE = "I didn't get that."
COOL_DONE_MESSAGE = "Ok, cool. Then we're done."
REQUESTED_SLOT_FIELD = "requested_slot"
YES_NO_BUTTONS = [
    {"title": BUTTON_YES, "payload": PAYLOAD_AFFIRM},
    {"title": BUTTON_NO, "payload": PAYLOAD_DENY},
]
LETS_RESUME_MESSAGE = "Ok, let's resume"

class ValidateFRMMakeNewBot(FormValidationAction):
    def name(self) -> Text:
        return "validate_FRM_make_new_bot"

    def validate_SL_bot_name(self, slot_value: Any, dispatcher: CollectingDispatcher, tracker: Tracker, domain: DomainDict,) -> Dict[Text, Any]: 
        """
            Validates the user's input for the bot name slot. If the user wants to abort the bot creation process, this function returns the appropriate slot values to indicate the abort request. Otherwise, it returns the bot name slot value as provided by the user.
        """

        # user wants to abort
        if tracker.get_intent_of_latest_message() == INTENT_ABORT:
            return {
                SLOT_BOT_NAME: None,
                SLOT_ABORT_MAKE_BOT_FLAG: None,
                REQUESTED_SLOT_FIELD: SLOT_ABORT_MAKE_BOT_FLAG
            }
        
        # set the new bot's name
        else: 
            return {SLOT_BOT_NAME: slot_value}

    def validate_SL_xconn_name(self, slot_value: Any, dispatcher: CollectingDispatcher, tracker: Tracker, domain: DomainDict, ) -> Dict[Text, Any]: 
        """
            Validates the user's input for the exchange connector name slot. If the user wants to abort the bot creation process, this function returns the appropriate slot values to indicate the abort request. Otherwise, it validates the provided exchange connector name and returns the slot value.
        """

        # user wants to abort
        if tracker.get_intent_of_latest_message() == INTENT_ABORT:
            return {
                SLOT_XCONN_NAME: None,
                SLOT_ABORT_MAKE_BOT_FLAG: None,
                REQUESTED_SLOT_FIELD: SLOT_ABORT_MAKE_BOT_FLAG
            }

        # validate exchange connector
        else: 
            try:    
                # instantiate user object
                razgar_user = RazgaRUI_factory(tracker.get_slot(SLOT_USER_ID), tracker.get_slot(SLOT_ENV))
                if not razgar_user.isXConnInList(slot_value):
                    # provided exchange connector was not found
                    dispatcher.utter_message(text=ERROR_NO_CONNECTOR.format(slot_value))
                    return {SLOT_XCONN_NAME: None}
                else: 
                    # set the chosen connector
                    return {SLOT_XCONN_NAME: slot_value}

            except Exception as e: 
                print(f"ERROR: {e}")            
                dispatcher.utter_message(text=f"{ERROR_API_ACCESS} (2)")
                #force stop making bot process in the NLU
                return {REQUESTED_SLOT_FIELD: None} 

    def validate_SL_xinstrument_name(self, slot_value: Any, dispatcher: CollectingDispatcher, tracker: Tracker, domain: DomainDict,) -> Dict[Text, Any]: 
        """
            Validates the user's input for the exchange instrument name slot. If the user wants to abort the bot creation process, this function returns the appropriate slot values to indicate the abort request. Otherwise, it validates the provided exchange instrument name and returns the slot value.

            This function performs the following steps:

            1. If the user wants to abort the bot creation process, it returns the appropriate slot values to indicate the abort request.
            2. If the user provides a valid exchange instrument name, it retrieves the corresponding instrument object from the database and returns the slot value.
            3. If the provided exchange instrument name is not valid, it displays an error message and returns the slot value as `None`.
            4. If there is an error accessing the API, it displays an error message and stops the bot creation process.
        """

        # user wants to abort
        if tracker.get_intent_of_latest_message() == INTENT_ABORT:
            return {
                SLOT_XINSTRUMENT_NAME: None,
                SLOT_ABORT_MAKE_BOT_FLAG: None,
                REQUESTED_SLOT_FIELD: SLOT_ABORT_MAKE_BOT_FLAG
            }

        # validate slot_value (i.e. the instrument name)
        try:   
            # user object instatiation
            razgar_user = RazgaRUI_factory(tracker.get_slot(SLOT_USER_ID), tracker.get_slot(SLOT_ENV))
            # get instrument object from database (if slot_value and exchange-connector are valid pair) or 0.
            xinstrument = razgar_user.getXInstrument(slot_value, tracker.get_slot(SLOT_XCONN_NAME)) 
            if not xinstrument:
                dispatcher.utter_message(text=ERROR_NO_INSTRUMENT.format(slot_value))
                return {SLOT_XINSTRUMENT_NAME: None}
        except Exception as e: 
            print(f"ERROR: {e}")            
            dispatcher.utter_message(text=f"{ERROR_API_ACCESS} (4)")
            #force stop making bot process in the NLU
            return {REQUESTED_SLOT_FIELD: None} 


        # make the bot
        try:
            xconn_id = razgar_user.getXConnByName(tracker.get_slot(SLOT_XCONN_NAME))[0]["id"]
            xinstrument_id = xinstrument['id']
            bot_id = razgar_user.makeBot(tracker.get_slot(SLOT_BOT_NAME), xconn_id, xinstrument_id)

        except Exception as e: 
            print(f"ERROR: {e}")            
            dispatcher.utter_message(text=f"{ERROR_API_ACCESS} (5)")
            #force stop making bot process in the NLU
            return {REQUESTED_SLOT_FIELD: None} 
        
        dispatcher.utter_message(text=SUCCESS_BOT_CREATED.format(bot_id))
        # reuse botname slot to store the generated bot id
        return {SLOT_XINSTRUMENT_NAME: slot_value, SLOT_BOT_NAME: bot_id} 
        
    def validate_SL_start_bot_flag(self,slot_value: Any, dispatcher: CollectingDispatcher, tracker: Tracker, domain: DomainDict, ) -> Dict[Text, Any]: 
        """
            Validates the user's intent to start the bot after the "Make Bot" process is complete. This function handles the following scenarios:

            1. If the user indicates they want to abort the process, the `SLOT_START_BOT_FLAG` and `SLOT_ABORT_MAKE_BOT_FLAG` slots are set to `None`, and the `REQUESTED_SLOT_FIELD` is set to `SLOT_ABORT_MAKE_BOT_FLAG` to prompt the user for confirmation.
            2. If the user confirms they want to start the bot, the bot is started using the `startBot` method of the `RazgaRUI` object, and the `SLOT_START_BOT_FLAG` is set to `True`.
            3. If the bot could not be started, an error message is displayed and the "Make Bot" process is stopped by setting the `REQUESTED_SLOT_FIELD` to `None`.
            4. If the user denies starting the bot, the `SLOT_START_BOT_FLAG` is set to `False`.
            5. If the user's intent is neither affirmation nor denial, a message is displayed and the `SLOT_START_BOT_FLAG` is set to `None` to prompt the user for confirmation again.

            This function is used to handle the user's decision to start or abort the bot after the "Make Bot" process is complete.
        """

        # user wants to abort
        if tracker.get_intent_of_latest_message() == INTENT_ABORT:
            return {
                SLOT_START_BOT_FLAG: None,
                SLOT_ABORT_MAKE_BOT_FLAG: None,
                REQUESTED_SLOT_FIELD: SLOT_ABORT_MAKE_BOT_FLAG
            }

        # user wants to start bot
        elif tracker.get_intent_of_latest_message() == INTENT_AFFFIRM:
            try:
                # instantiate user object
                razgar_user = RazgaRUI_factory(tracker.get_slot(SLOT_USER_ID), tracker.get_slot(SLOT_ENV))
                # start bot request
                razgar_user.startBot(tracker.get_slot(SLOT_BOT_NAME))
                # display success message
                dispatcher.utter_message(text=SUCCESS_BOT_RUNNING.format(tracker.get_slot(SLOT_BOT_NAME)))
                return {SLOT_START_BOT_FLAG: True}
            
            except RuntimeError as e:
                # display error
                dispatcher.utter_message(text=BOT_COULD_NOT_START_CRASH)
                dispatcher.utter_message(text=str(e))
                #force stop making bot process in the NLU
                return {REQUESTED_SLOT_FIELD: None}

            except Exception as e: 
                print(f"ERROR: {e}")            
                dispatcher.utter_message(text=ERROR_BOT_START_API)
                #force stop making bot process in the NLU
                return {REQUESTED_SLOT_FIELD: None} 

        # user does not want to start the bot
        elif tracker.get_intent_of_latest_message() == INTENT_DENY:
            dispatcher.utter_message(text=COOL_DONE_MESSAGE)
            return {SLOT_START_BOT_FLAG: False}
        
        # neither affirmed, nor denied. Did not understand
        else: 
            dispatcher.utter_message(text=DIDNT_GET_THAT_MESSAGE)
            # prompt again
            return {SLOT_START_BOT_FLAG: None}

    def validate_SL_abort_make_bot_flag(self, slot_value: Any, dispatcher: CollectingDispatcher, tracker: Tracker, domain: DomainDict,) -> Dict[Text, Any]: 
        """
            Validates the user's intent to abort the "Make Bot" process. This function handles the following scenarios:

            1. If the user indicates they still want to make a new bot, the `SLOT_ABORT_MAKE_BOT_FLAG` is set to `False`.
            2. If the user confirms they want to abort the process, a message is displayed and the "Make Bot" process is stopped by setting the `REQUESTED_SLOT_FIELD` to `None`.
            3. If the user denies aborting the process, the `SLOT_ABORT_MAKE_BOT_FLAG` is set to `False` to resume the "Make Bot" process.
            4. If the user's intent is neither affirmation nor denial, a message is displayed and the `SLOT_ABORT_MAKE_BOT_FLAG` is set to `None` to prompt the user for confirmation again.

            This function is used to handle the user's decision to abort or continue the "Make Bot" process during the conversation.
        """

        # user still wants to make a bot
        if(tracker.get_intent_of_latest_message() == INTENT_WANTS_NEW_BOT):
            return {SLOT_ABORT_MAKE_BOT_FLAG: False}

        # user confirmed abortion
        elif tracker.get_intent_of_latest_message() == INTENT_AFFFIRM:
            dispatcher.utter_message(text=MAKING_BOT_CANCELLED_MESSAGE)
            #force stop making bot process in the NLU
            return {REQUESTED_SLOT_FIELD: None}

        # user denied abortion 
        elif tracker.get_intent_of_latest_message() == INTENT_DENY:
            dispatcher.utter_message(text=LETS_RESUME_MESSAGE)
            return {SLOT_ABORT_MAKE_BOT_FLAG: False}

        # neither affirmed, nor denied. Did not understand
        else:
            dispatcher.utter_message(text=DIDNT_GET_THAT_MESSAGE) 
            # prompt again for confirmation
            return {
                SLOT_ABORT_MAKE_BOT_FLAG: None,
                REQUESTED_SLOT_FIELD: SLOT_ABORT_MAKE_BOT_FLAG
            } 



class ActionAskSLXConnName(Action):
    """
        Asks the user to select an exchange connector from a list of available options.
        
        This action is responsible for displaying a prompt to the user, asking them to select an exchange connector from a list of available options. The list of options is retrieved from the RazgaRUI object based on the user's ID and environment.
        
        If the user has no exchange connectors, an error message is displayed and the "Make Bot" process is stopped.        
    """
    def name(self) -> Text:
        return "action_ask_SL_xconn_name"

    def run(self, dispatcher: CollectingDispatcher, tracker: Tracker, domain: Dict) -> List[EventType]:

        try:
            # instantiate user object
            razgar_user = RazgaRUI_factory(tracker.get_slot(SLOT_USER_ID), tracker.get_slot(SLOT_ENV))
            # fetch user's exchange connectors
            xconns = razgar_user.getXConnList()
            if len(xconns) == 0:
                # the user has no exchange connectors
                dispatcher.utter_message(text=ERROR_NO_CONNECTORS)
                #force stop making bot process in the NLU
                return [SlotSet(REQUESTED_SLOT_FIELD, None)] 
            else:
                # format exchange connector list to buttons list
                buttons = Helper.mapXConnsListToButtons(xconns)
                # display prompt with buttons
                dispatcher.utter_message(text=CHOOSE_CONNECTOR_MESSAGE, buttons=buttons)
                return []

        except Exception as e: 
            print(f"ERROR: {e}")            
            dispatcher.utter_message(text=f"{ERROR_API_ACCESS} (7)")
            #force stop making bot process in the NLU
            return [SlotSet(REQUESTED_SLOT_FIELD, None)] 
        
class ActionAskSLXInstrumentName(Action):
    """
        Asks the user to select an exchange instrument from a list of available options based on the previously selected exchnage connector.        
    """
    def name(self) -> Text:
        return "action_ask_SL_xinstrument_name"

    def run(self, dispatcher: CollectingDispatcher, tracker: Tracker,  domain: Dict) -> List[EventType]:

        try:
            # instantiate user object
            razgar_user = RazgaRUI_factory(tracker.get_slot(SLOT_USER_ID), tracker.get_slot(SLOT_ENV))
            # get possible options based on connector
            xinstruments = razgar_user.getXInstrumentListByXConnName(tracker.get_slot(SLOT_XCONN_NAME)) # TODO: what if the xinstruments is empty?
            # format to buttons list
            buttons = Helper.mapXInstrumentsListToButtons(xinstruments)
            # display prompt with buttons
            dispatcher.utter_message(text=CHOOSE_INSTRUMENT_PROMPT, buttons=buttons)
            return []

        except Exception as e: 
            print(f"ERROR: {e}")            
            dispatcher.utter_message(text=f"{ERROR_API_ACCESS} (9)")
            #force stop making bot process in the NLU
            return [SlotSet(REQUESTED_SLOT_FIELD, None)] 

class ActionAskSLStartBotFlag(Action):
    """
        Asks the user whether they want to start the bot after it has been created.
        
        This action is responsible for displaying a prompt to the user, asking them to confirm whether they want to start the bot that was just created. The prompt includes two buttons, one for "Yes" and one for "No".
        
        The result of the user's response is returned as a list of events, which can be used to update the state of the conversation.
    """
    
    def name(self) -> Text:
        return "action_ask_SL_start_bot_flag"

    def run(self, dispatcher: CollectingDispatcher, tracker: Tracker, domain: Dict) -> List[EventType]:

        # display prompt with buttons
        dispatcher.utter_message(text=START_BOT_PROMPT, buttons=YES_NO_BUTTONS)
        return []

class ActionAskSLAbortMakeBotFlag(Action):
    """
        Asks the user to confirm whether they want to abort the "Make Bot" process.
        
        This action is responsible for displaying a prompt to the user, asking them to confirm whether they want to abort the current "Make Bot" process. The prompt includes two buttons, one for "Yes" and one for "No".
        
        The result of the user's response is returned as a list of events, which can be used to update the state of the conversation.
    """

    def name(self) -> Text:
        return "action_ask_SL_abort_make_bot_flag"

    def run(self, dispatcher: CollectingDispatcher, tracker: Tracker, domain: Dict) -> List[EventType]:

        # display prompt with buttons
        dispatcher.utter_message(text=ABORT_CONFIRMATION_PROMPT, buttons=YES_NO_BUTTONS)
        return []

class ActionCleanMakeBotSlots(Action):
    """
        Resets the slots related to the "Make Bot" functionality in the Rasa chatbot.

        This action is responsible for clearing the following slots:
        - SLOT_BOT_NAME: The name of the bot being created.
        - SLOT_XCONN_NAME: The name of the X-Connection used for the bot.
        - SLOT_XINSTRUMENT_NAME: The name of the X-Instrument used for the bot.
        - SLOT_START_BOT_FLAG: A flag indicating whether the bot should be started.
        - SLOT_ABORT_MAKE_BOT_FLAG: A flag indicating whether the "Make Bot" process should be aborted.

        This action is typically used to reset the state of the "Make Bot" functionality when the user wants to start a new bot creation process or cancel the current one.
    """

    def name(self) -> Text:
        return "action_clean_make_bot_slots"

    def run(self, dispatcher: CollectingDispatcher, tracker: Tracker, domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:

        # set all slots to NONE
        return [
            SlotSet(SLOT_BOT_NAME, None), 
            SlotSet(SLOT_XCONN_NAME, None),
            SlotSet(SLOT_XINSTRUMENT_NAME, None), 
            SlotSet(SLOT_START_BOT_FLAG, None), 
            SlotSet(SLOT_ABORT_MAKE_BOT_FLAG, False)
        ]
