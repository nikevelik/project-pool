#pragma once
#include "../assets/Vector.hpp"
#include "../assets/String.h"
#include "Operation.h"
#include "Session.h"
class Application{
public:
    void help() const;
    void load(const String& s);
    void change(int id);
    void close();
    void sessioninfo() const ;
    void undo();
    void save();
    void saveas(const String& s);
    void collage(const String&  s1, const String& s2, bool isVertical, const String& s3);
    void operate(const Operation& op);
    void run();
    void add(const String& s);
private: 
    bool parse(const String& cmd);
    Vector<Session> sess;
    int curr = -1;
};
