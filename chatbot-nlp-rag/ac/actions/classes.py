
DB_01_CONFIG = [
    {
    "host": '',
    "database": '',
    "user": '',
    "password": '',
    },
    {
    "host": '',
    "database": '',
    "user": '',
    "password": '',
    },
    {
    "host": '',
    "database": '',
    "user": '',
    "password": '',
    },
]

URL_CONFIG = [
    "",
    "",
    "",
]

URL_ADD_BOT = ""
URL_CONTROL_BOT = ""
API_SUCCESS_CODE = 1
FIELD_COMMUNICATION_CODE = "communicationCode"
FIELD_LAST_CRASH_TEXT = "last_crash_text"
SECRET_PASS_PHRASE = ""

from mysql import connector
from enum import Enum
import json
import requests

class Environment(Enum):
    DEV = 0
    TEST = 1
    APP = 2

class RazgaRUI:
    def __init__(self, UID, ENV):
        self.UID = UID
        self.ENV = ENV

    @staticmethod
    def EXECUTESQL(db_conf, query, params=None):     
        with connector.connect(**db_conf) as conn:
            if conn.is_connected():
                cursor = conn.cursor(dictionary=True)
                cursor.execute(query, params)
                results = cursor.fetchall()
                cursor.close()
                return results
            else:
                raise RuntimeError("Failed to connect to the database.")
    
    def getXConnList(self):
        return self.EXECUTESQL(DB_01_CONFIG[self.ENV.value], "SELECT * FROM `exchange_connectors` WHERE investor_id = %s", (self.UID,))
    
    def getXConnByName(self, name):
        return self.EXECUTESQL(DB_01_CONFIG[self.ENV.value], "SELECT * FROM `exchange_connectors` WHERE investor_id = %s AND title = %s", (self.UID, name))
    
    def isXConnInList(self, name):
        xconns = self.getXConnList()
        for entry in xconns:
            if entry.get('title') == name:
                return True
        return False

    def getXInstrumentListByXID(self, x_id):
        return self.EXECUTESQL(DB_01_CONFIG[self.ENV.value], "SELECT * FROM `exchange_instruments` WHERE exchange_id = %s", (x_id,))

    def getXInstrumentListByXConnName(self, xconn_name):
        x_conn = self.getXConnByName(xconn_name)
        x_id = x_conn[0]["exchange_id"]
        return self.getXInstrumentListByXID(x_id)
    
    def getXInstrument(self, xinstrument_name, xconn_name):
        xinstruments = self.getXInstrumentListByXConnName(xconn_name)
        for entry in xinstruments:
            if entry.get('name') == xinstrument_name:
                return entry
        return 0

    def makeBot(self, title, xconn_id, xinstrument_id):
        response = requests.post(url=URL_CONFIG[self.ENV.value]+URL_ADD_BOT, data={
            'title': title,
            'type': '0',
            'exchangeConnector': xconn_id,
            'symbol': xinstrument_id,
            'section': '0',
            'user_id': self.UID,
            'static_auth': SECRET_PASS_PHRASE
        })
        json_obj = json.loads(response.text)
        if json_obj[FIELD_COMMUNICATION_CODE] == API_SUCCESS_CODE and json_obj["id"] > 0:
            return json_obj["id"]
        else:
            raise RuntimeError("Failed to create bot. Error code: " + json_obj[FIELD_COMMUNICATION_CODE])
         
    def startBot(self, bot_id):
        response = requests.post(url=URL_CONFIG[self.ENV.value]+URL_CONTROL_BOT, data={    
            'bot_id': bot_id,
            'action': 'start',
            'user_id': self.UID,
            'static_auth': SECRET_PASS_PHRASE
        })
        json_obj = json.loads(response.text)
        if json_obj[FIELD_COMMUNICATION_CODE] != API_SUCCESS_CODE:
            raise RuntimeError(json_obj[FIELD_LAST_CRASH_TEXT] if FIELD_LAST_CRASH_TEXT in json_obj else "Unknown error")


class Helper:
    @staticmethod
    def mapXConnsListToButtons(xconns):
        return [{"title": entry.get('title'), "payload": entry.get('title')} for entry in xconns]
    
    @staticmethod
    def mapXInstrumentsListToButtons(xinstruments):
        buttons = []
        allowed_instruments = ["BTC-PERPETUAL", "ETH-PERPETUAL", "SOL-PERPETUAL"]
        count = 0
        for entry in xinstruments:
            name = entry.get('name')
            if name in allowed_instruments:
                buttons.append({"title": name, "payload": name})
                count += 1
                if count >= 3:  
                    break
        for entry in xinstruments:
            name = entry.get('name')
            if name not in allowed_instruments:
                buttons.append({"title": name, "payload": name})
                count += 1
                if count >= 5:  
                    break
        return buttons

    @staticmethod
    def convertEnv(env):
        if env in [0, "0"]:
            return Environment.DEV
        elif env in [1, "1"]:
            return Environment.TEST
        elif env in [2, "2"]:
            return Environment.APP
        else:
            raise ValueError(f"Invalid environment value: {env}")

def RazgaRUI_factory(UID, ENV):
    return RazgaRUI(UID, Helper.convertEnv(ENV))