#pragma once
#include "Shade.h"
#include<fstream>
#include <stdexcept>
unsigned Shade::MAX_ALLOWED = 255;
const float SHADE_FACTOR = 1.7;
bool Shade::isCorrect(unsigned char maxvalue) const {
    return v <= maxvalue;
}

bool Shade::isMonochrome(unsigned char maxvalue) const {
    return v == 0 || v == maxvalue;
}

void Shade::monochrome(unsigned char maxvalue) {
    if(!isMonochrome(maxvalue)){
        v =( (SHADE_FACTOR*v >=maxvalue/2) ? maxvalue : 0);
    }
}

void Shade::negative(unsigned char maxvalue){
    v = maxvalue - v;
}

std::ifstream& operator>>(std::ifstream& ifs, Shade& obj){
    int v;
    ifs >> v;
    Shade::check(ifs, v);
    obj.v = v;
    return ifs;
}


std::ofstream& operator<<(std::ofstream& ofs,const Shade& obj){
    int v;
    v = obj.v;
    ofs << v << " ";
    return ofs;
}


void Shade::check(std::ifstream& ifs, int v){
    if(!ifs.good()){
        throw std::runtime_error("could not read pixel values from source.");
    }
    if(v > Shade::MAX_ALLOWED || v < 0){
        throw std::runtime_error("Input value is not a pixel value");
    }
}