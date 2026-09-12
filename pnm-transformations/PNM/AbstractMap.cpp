#pragma once
#include "Grayness.h"
#include"AbstractMap.h"
#include<fstream>
#include <stdexcept>

void AbstractMap::deserialize(const String& filename){
    fn = filename;
    std::ifstream infile(filename.c_str(), std::ios::binary);
    if (!infile) {
        throw std::runtime_error("Error! Could not open file");   
    }
    deserializeheader(infile);
    switch(raw){
        case true:
            deserializeraw(infile);
            break;
        case false: 
            deserializeplain(infile);
            break;
    }
}

void AbstractMap::serialize(const String& filename) const {

    switch(raw){
        case true:
            serializeraw(filename);
            break;
        case false: 
            serializeplain(filename);
            break;
    }
}


void AbstractMap::serialize() const {
    if(mod){
        // std::cout << "MOD " << fn << "\n"; 
        serialize(fn);
    }
    // else{
        // std::cout << "No modification needed for " << fn << "\n"; 
    // }
    mod = false;
}

bool AbstractMap::isGray() const {
    return g>Grayness::Colorful;
}

bool AbstractMap::isMonochrome() const {
    return g==Grayness::Monochrome;
}

const String& AbstractMap::getFilename() const{
    return fn;
}