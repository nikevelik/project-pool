#pragma once
#include"Pixel.h"
#include<fstream>
#include <stdexcept>

unsigned Pixel::MAX_ALLOWED = 255;
const float RFACTOR = 0.5125;
const float GFACTOR = 0.8154;
const float BFACTOR = 0.3721;
void Pixel::check(std::ifstream& ifs, int v){
    if(!ifs.good()){
        throw std::runtime_error("could not read pixel values from source.");
    }
    if(v > Pixel::MAX_ALLOWED || v < 0){
        throw std::runtime_error("Input value is not a pixel value");
    }
}

std::ifstream& operator>>(std::ifstream& ifs, Pixel& obj){
    int v;
    ifs >> v;
    Pixel::check(ifs, v);
    obj.r = v;

    ifs >> v;
    Pixel::check(ifs, v);
    obj.g = v;

    ifs >> v;
    Pixel::check(ifs, v);
    obj.b = v;
    
    return ifs;
}


std::ofstream& operator<<(std::ofstream& ofs,const Pixel& obj){
    int v;
    v = obj.r;
    ofs << v << " ";


    v = obj.g;
    ofs << v << " ";


    v = obj.b;
    ofs << v << "\n";
    
    return ofs;
}

bool Pixel::isCorrect(unsigned char maxvalue) const{
    return r <= maxvalue && g <= maxvalue && b<=maxvalue;
}

bool Pixel::isGray() const{
    return r==b && r==g;
}
bool Pixel::isMonochrome(unsigned char maxvalue) const {
    return (r==0 && b==0 && g==0) || (r==maxvalue && b==maxvalue && g==maxvalue);
}
void Pixel::grayscale(){
    if(!isGray()){
        int sum = r+b+g;
        r = b = g = sum/3;
    }
}
void Pixel::monochrome(unsigned char maxvalue){
    if(!isMonochrome(maxvalue)){
        r = b = g = ((((RFACTOR*r + GFACTOR*g + BFACTOR*b)) >= maxvalue/2) ? maxvalue : 0);
    }
}
void Pixel::negative(unsigned char maxvalue){
    r = maxvalue - r;
    g = maxvalue - g;
    b = maxvalue - b;
}