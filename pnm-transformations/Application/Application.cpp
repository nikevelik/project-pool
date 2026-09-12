#pragma once
#include"Application.h"
#include<sstream>
void Application::help() const {
    std::cout << "Available commands:\n";
    std::cout << "  help                 - Display this help information.\n";
    std::cout << "  switch <session_id>  - Switch to a different session by ID.\n";
    std::cout << "  close                - Close the current session.\n";
    std::cout << "  session              - Display information about the current session.\n";
    std::cout << "  undo                 - Undo the last action.\n";
    std::cout << "  quit                 - Exit the application.\n";
    std::cout << "  save                 - Save the current session.\n";
    std::cout << "  load <filename>      - Load a session from a file.\n";
    std::cout << "  add <filename>       - Add a file to the current session.\n";
    std::cout << "  saveas <filename>    - Save the current session as a new file.\n";
    std::cout << "  grayscale            - Apply a grayscale filter to the current image.\n";
    std::cout << "  monochrome           - Apply a monochrome filter to the current image.\n";
    std::cout << "  negative             - Apply a negative filter to the current image.\n";
    std::cout << "  collage <orientation> <file1> <file2> <file3> - Create a collage from two images.\n";
    std::cout << "                          orientation: 'vertical' or 'horizontal'\n";
    std::cout << "  rotate <direction>   - Rotate the current image.\n";
    std::cout << "                          direction: 'left' or 'right'\n";
}

void Application::load(const String& s){
    try{
        sess.pushBack(Session(s));
        curr = sess.getSize()-1;
        std::cout << "Session with ID: " << sess[curr].getID() << " started\n";
    }catch(std::exception& e){
        std::cerr << "Did not start session due: " << e.what();
    }
}

void Application::change(int id){

    for(int i = 0; i < sess.getSize(); i++){
        if(sess[i].getID() == id){
            std::cout << "switching to session " << id << "\n";
            curr = i;
            return;
        }
    }
    std::cerr << "not valid session id\n";
    return;        
}

void Application::add(const String& s){
    if(curr == -1){
        std::cerr << "cannot add in non-existend session\n";
        return;
    }
    sess[curr].add(s);
}

void Application::close(){
    if(curr == -1){
        std::cerr << "cannot close non-existend session\n";
        return;
    }
    sess.popAt(curr);
    curr = -1;
    std::cout << "Session Closed\n";
}

void Application::sessioninfo() const {
    if(curr==-1){
        std::cerr << "cannot display info of non-existend session\n";
        return;
    }
    std::cout << "sessionID: " << curr << "\nadded images: ";
    sess[curr].info();

}

void Application::undo(){
    if(curr==-1){
        std::cerr << "cannot display udno in non-existend session\n";
        return;
    }
    sess[curr].undo();
}

void Application::save(){
    if(curr==-1){
        std::cerr << "cannot save in non-existend session\n";
        return;
    }
    sess[curr].save();

}
void Application::saveas(const String& s){
    if(curr==-1){
        std::cerr << "cannot save in non-existend session\n";
        return;
    }
    sess[curr].saveas(s);
}
void Application::collage(const String&  s1, const String& s2, bool isVertical, const String& s3){
    if(curr==-1){
        std::cerr << "cannot collage in non-existend session\n";
        return;
    }
    sess[curr].collage(s1, s2, isVertical, s3);
}
void Application::operate(const Operation& op){
    if(curr==-1){
        std::cerr << "cannot display operate in non-existend session\n";
        return;
    }
    sess[curr].operate(op);
}


bool Application::parse(const String& cmd) {
    std::istringstream iss(cmd.c_str());
    String command;
    iss >> command;

    if (command == "help") {
        help();
        return 1;
    }

    if (command == "switch") {
        int sessionId;
        iss >> sessionId;
        change(sessionId);
        return 1;
    }

    if (command == "close") {
        close();
        return 1;
    }


    if (command == "session") {
        sessioninfo();
        return 1;
    }

    if (command == "undo") {
        undo();
        return 1;
    }

    if (command == "quit") {
        std::cout << "exited!\n";
        return 0;
    }

    if (command == "save") {
        save();
        return 1;
    }

    if (command == "load") {
        String filename;
        iss >> filename;
        load(filename.c_str());
        return 1;
    }

    if (command == "add") {
        String filename;
        iss >> filename;
        add(filename);
        return 1;
    }

    if (command == "saveas") {
        String filename;
        iss >> filename;
        saveas(filename);
        return 1;
    }

    if (command == "grayscale") {
        operate(Operation::Grayscale);
        return 1;
    }

    if (command == "monochrome") {
        operate(Operation::Monochrome);
        return 1;
    }

    if (command == "negative") {
        operate(Operation::Negative);
        return 1;
    }

    if (command == "collage") {
        String orientation, file1, file2, file3;
        iss >> orientation >> file1 >> file2 >> file3;
        bool isVertical = (orientation == "vertical");
        collage(file1, file2, isVertical, file3);
        return 1;
    }


    if (command == "rotate") {
        String direction;
        iss >> direction;
        if (direction == "left") {
            operate(Operation::RotationL);
        } else if (direction == "right") {
            operate(Operation::RotationR);
        }else {
            std::cerr << "invalid command\n";
        }
        return 1;
    }

    std::cerr << "invalid command\n";
    return 1;
}


void Application::run(){
    char buff[1024];
    while(std::cin){
        std::cin.getline(buff, 1024);
        if(!parse(buff)){
            break;
        }
    }
}