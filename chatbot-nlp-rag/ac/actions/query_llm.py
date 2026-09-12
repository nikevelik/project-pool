from groq import Groq

client = Groq(api_key="")

def query_llm(query_text):
    chat_completion = client.chat.completions.create(
        messages=[
            {
                "role": "user",
                "content": query_text,
            }
        ],
        model="llama3-8b-8192",
    )

    return (chat_completion.choices[0].message.content)