import random

class NameMaker:
    expressions = [
        "Ouch", "Wow", "Yikes", "Oops", "Ugh", "Aha", "Yay", "Eek", "Eh", "Phew", 
        "Hooray", "Huh", "Hmm", "Whoa", "Gah", "Oof", "Yuck", "Nope", "Aww", "Pfft", 
        "Boo", "Ah", "Hah", "Yip", "Ugh", "Zing", "Blah", "Aye", "Uh-oh", "Tsk", 
        "Hmph", "Eww", "Whee", "Gee", "Neh", "Woot", "Grr", "Oi", "Hoor", "Hype", 
        "Nah", "D'oh", "Rarr", "Hah", "Poo", "Brr", "Zzz", "Ahem", "Ehh", "Vroom"
    ]
    
    @staticmethod
    def new():
        num_expressions = random.randint(1, 5)
        return ''.join(random.choices(NameMaker.expressions, k=num_expressions))