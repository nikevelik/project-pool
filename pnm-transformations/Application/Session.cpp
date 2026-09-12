#pragma once
#include"Session.h"
#include<iostream>
#include<stdexcept>
unsigned Session::lastid = 1;

unsigned Session::getID() const noexcept {
    return id;
}

void Session::collage(const String& i1, const String& i2, bool isVertical, const String& out) noexcept{
    std::cerr << "Collages are not available.\n";
}

void Session::calc(const Operation& op, int& r, Grayness& g, bool& n) noexcept{
    switch (op){
        case Operation::Grayscale:
            g = ((g == Grayness::Monochrome) ? Grayness::Monochrome : Grayness::Grayscale);
            break;
        case Operation::Monochrome:
            g = Grayness::Monochrome;
            break;
        case Operation::Negative:
            n = !n;
            break;
        case Operation::RotationL:
            r = (r + 3)%4;
            break;
        case Operation::RotationR:
            r = (r + 1)%4;
            break;
    }
}

void Session::apply(AbstractMap* target, int r, Grayness g, bool n) noexcept{
    try{
        if(g == Grayness::Monochrome){
            target->monochrome();
        }else if(g == Grayness::Grayscale){
            target->grayscale();
        }
        if(r == 1){
            target->rotation90();
        }else if(r == 2){
            target->rotation180();
        }else if(r == 3){
            target->rotation270();
        }
        if(n){
            target->negative();
        }
    }catch(std::exception& e){
        try{
            target->deserialize(target->getFilename());
            std::cerr << "Could not apply a transformation to file:" + target->getFilename() + ". Please try again.";
        }catch(std::exception& e){
            std::cerr << "Could not apply a transformation to file:"+target->getFilename() +" AND could not return it to default state. Please discard your changes and try again later.";
        }
    }
}

void Session::info() const noexcept{
    for(unsigned i = 0; i < files.getSize(); i++){
        std::cout << files[i].getFirst() << " ";
    }
    std::cout << "\nunsaved operations:";
    for(unsigned i = 0; i < operations.getSize(); i++){
        std::cout << operations[i] << " ";
    }
    std::cout << "\n";
}

Session::Session(const String& filename){
    id = lastid++;
    if(isValidFileType(filename)){
        files.pushBack(Pair<String, unsigned>(filename, operations.getSize()));
        std::cout << "Image \"" << filename <<"\" added\n";
    }else{
        throw std::runtime_error("file name is not valid\n");
    }
}

void Session::add(const String& filename) noexcept{
    if(isValidFileType(filename)){
        for(unsigned i = 0; i <files.getSize(); i++){
            if(files[i].getFirst() == filename){
                std::cout << "file is already in the session\n";
                return;
            }
        }
        for(unsigned i = 0; i <img.getSize(); i++){
            if(img[i]->getFilename() == filename){
                std::cout << "file is already in the session\n";
                return;
            }
        }
        files.pushBack(Pair<String, unsigned>(filename, operations.getSize()));
        std::cout << "Image \"" << filename <<"\" added\n";
    }else{
        std::cerr << "file name is not valid\n";
        return;
    }
}

void Session::undo() noexcept{
    if(operations.getSize()==0 && files.getSize() <= 1 && img.getSize() == 0){
        std::cout << "nothing to undo\n";
        return;
    }
    if(files.getSize()>0 && files[files.getSize()-1].getSecond()==operations.getSize()){
        files.popBack();
        std::cout << "adding file undo\n";
    }else if(operations.getSize()>0){
        operations.popBack();
        std::cout << "last transformation undone\n";
    }else{
        std::cout << "nothing to undo\n";
    }
}

void Session::operate(const Operation& op) noexcept{
    operations.pushBack(op);
    std::cout << "operation added to list\n";
}

void Session::save()noexcept{
    unsigned oldSize = img.getSize();
    for(int i = 0; i < files.getSize(); i++){
        try{
            img.add(files[i].getFirst().c_str());
        }catch(std::exception& e){
            std::cerr << "Error. Image " + files[i].getFirst() + " could not be open for modifications and is therefore excluded from the session.\n";
            files.popAt(i);
        }
    }
    int j = files.getSize()-1;
    int r = 0;
    Grayness g = Grayness::Colorful;
    bool n = 0;
    for(int i = operations.getSize()-1; i>=0; i--){
        calc(operations[i], r, g, n);
        if(r == 0 && g == Grayness::Colorful && n ==0){
            continue;
        }
        while(j>=0 && files[j].getSecond()>i){
            j--;
        }
        while(j>= 0 && files[j].getSecond()==i){
            apply(img[oldSize+j], r, g, n);
            try{
                img[oldSize+j]->serialize();
            }catch(std::exception& e){
                std::cerr << "Could not serialize file: " + img[oldSize+j]->getFilename() + ". Please discard your changes and try again later";
            }
            j--;
        }
    }
    operations.clear();
    files.clear();
    for(int i = 0; i < oldSize; i++){
        apply(img[i], r, g, n);
        try{
            img[i]->serialize();
        }catch(std::exception& e){
            std::cerr << "Could not serialize file: " + img[i]->getFilename() + ". Please discard your changes and try again later";
        }
    }
    std::cout << "Saving Completed!\n";
}

void Session::saveas(const String& filename) const noexcept{
    try{
        int r = 0;
        Grayness g = Grayness::Colorful;
        bool n = 0;
        AbstractMap* res;
        if(img[0]){
             res = img[0]->clone();
        }else{
            res = PNMFactory::create(files[0].getFirst().c_str());
        }
        for(int i = operations.getSize()-1; i>=0; i--){
            calc(operations[i], r, g, n);
        }
        apply(res, r, g, n);
        res->serialize(filename);
        std::cout << "Saving Completed!\n";
    }catch(std::exception& e){
        std::cerr << "Could not perform saveas. Err: \"" << e.what() <<"\"\n";
    }
    
}