import argparse
from langchain_community.vectorstores import Chroma
from langchain_community.embeddings import FastEmbedEmbeddings
from langchain.prompts import ChatPromptTemplate
from .query_llm import query_llm
import os
import sys
__import__('pysqlite3')
sys.modules['sqlite3'] = sys.modules.pop('pysqlite3')

PRINT_PROMPT_DEBUG = False
CONTEXT_K_FACTOR = 2
CHROMA_PATH = os.path.join(os.path.dirname(__file__), 'chroma')
PROMPT_TEMPLATE_PATH = os.path.join(os.path.dirname(__file__), 'prompt_template.txt')
CONFIG_PATH = os.path.join(os.path.dirname(__file__), 'conf.txt')

def load_prompt_template(file_path):
    with open(file_path, 'r') as file:
        return file.read()

def load_config(config_path):
    global PRINT_PROMPT_DEBUG, CONTEXT_K_FACTOR
    with open(config_path, 'r') as file:
        for line in file:
            if line.startswith('PRINT_PROMPT_DEBUG'):
                PRINT_PROMPT_DEBUG = line.split('=')[1].strip().lower() == 'true'
            elif line.startswith('CONTEXT_K_FACTOR'):
                CONTEXT_K_FACTOR = int(line.split('=')[1].strip())

PROMPT_TEMPLATE = load_prompt_template(PROMPT_TEMPLATE_PATH)
CHUNK_SEPARATOR_WITHIN_CONTEXT = "\n---\n"
load_config(CONFIG_PATH)


def query_rag(query_text):
    embedding_function = FastEmbedEmbeddings()
    db = Chroma(persist_directory=CHROMA_PATH, embedding_function=embedding_function)


    results = db.similarity_search_with_relevance_scores(query_text, k=CONTEXT_K_FACTOR)



    context_text = CHUNK_SEPARATOR_WITHIN_CONTEXT.join([doc.page_content for doc, _score in results])
    prompt_template = ChatPromptTemplate.from_template(PROMPT_TEMPLATE)
    prompt = prompt_template.format(context=context_text, question=query_text)



    if PRINT_PROMPT_DEBUG:
        return "\n --- \nPROMPT START: \n --- \n" +  prompt + "\n --- \nEND OF PROMPT \n --- \n" + query_llm(prompt)
    else:
        return query_llm(prompt)
