import requests
import json

from typing import Any, Text, Dict, List

from rasa_sdk import Tracker, FormValidationAction, Action
from rasa_sdk.executor import CollectingDispatcher
from rasa_sdk.events import EventType, SlotSet
from rasa_sdk.types import DomainDict

from .query_rag import query_rag

class ActionAnswerWithLLM(Action):

    def name(self) -> Text:
        return "answer_with_llm"

    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:

        print("call!\n")

        dispatcher.utter_message(query_rag(tracker.latest_message.get('text', 'Default question')))

        return []
