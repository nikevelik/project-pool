import argparse
from langchain_community.vectorstores import Chroma
from langchain_community.embeddings import FastEmbedEmbeddings
from langchain.prompts import ChatPromptTemplate

from groq import Groq
import os

from groq import Groq

__import__('pysqlite3')
import sys
sys.modules['sqlite3'] = sys.modules.pop('pysqlite3')


CHROMA_PATH = "../actions/chroma"

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("query_text", type=str, help="The query text.")
    args = parser.parse_args()
    query_text = args.query_text

    embedding_function = FastEmbedEmbeddings()
    db = Chroma(persist_directory=CHROMA_PATH, embedding_function=embedding_function)

    results = db.similarity_search_with_relevance_scores(query_text, k=2)

    print("PROMPT-START")
    print('\n---\n'.join(doc.page_content for doc, _score in results))
    print("PROMPT-END")


if __name__ == "__main__":
    main()

