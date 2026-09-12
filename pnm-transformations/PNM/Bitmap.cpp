#pragma once
#include "Bitmap.h"
#include "Grayness.h"
#include <stdexcept>
#include <fstream>

const unsigned ALIGNMENT_ADDITIVE = 7;
const unsigned ALIGNMENT_FACTOR = 8;

Bitmap::Bitmap(const String& filename){
    g = Grayness::Monochrome;
    deserialize(filename);
}

void Bitmap::deserializeheader(std::ifstream& infile){
    String magic;
    infile >> magic;
    if (magic == MagicValue<Bit>::plain()) {
        raw = false;
    }else if(magic == MagicValue<Bit>::raw()){
        raw = true;
    }else{
        throw std::runtime_error("Invalid magic number for Bitmap files");
    }
    infile >> w >> h;
    if(!infile.good()){
        throw std::runtime_error("Invalid header values in file");
    }
    data = Bitset(h*(w+ALIGNMENT_ADDITIVE)/ALIGNMENT_FACTOR);
    char newline;
    infile.get(newline);
    if(!infile.good() || newline != '\n'){
        throw std::runtime_error("No separation between header and binary data. File is invalid");
    }
}

void Bitmap::deserializeplain(std::ifstream& infile){
    unsigned len = w*h;
    for(unsigned i = 0; i < len; i++){
        bool pixel;
        infile >> pixel;
        if(pixel){
            data.set(i);
        }
    }
}

void Bitmap::deserializeraw(std::ifstream& infile){
    infile.read(data.ptr(), data.size());

    if(raw){
        serializeplain(fn);
        deserialize(fn);
    }
}

void Bitmap::serializeraw(const String& filename) const {
    std::ofstream outfile(filename.c_str(), std::ios::binary);
    if (!outfile) {
        throw std::runtime_error("Error: Could not open file for writing.");
    }

    outfile << MagicValue<Bit>::raw() << "\n";

    outfile << w << " " << h << "\n";

    outfile.write(data.ptr(), data.size());

    outfile.close();
}

void Bitmap::serializeplain(const String& filename) const {
    std::ofstream outfile(filename.c_str());
    if (!outfile) {
        throw std::runtime_error("Error: Could not open file for writing.");
    }

    outfile << MagicValue<Bit>::plain() << "\n";
    outfile << w << " " << h << "\n";


    unsigned len = w*h;
    for(unsigned i = 0; i < len; i++){
        outfile << data.read(i) << " ";
        if((i+1)%w == 0){
            outfile << "\n";
        }
    }

    outfile.close();
}

void Bitmap::grayscale() {
    return;
}
void Bitmap::monochrome() {
    return;
}
void Bitmap::negative() {
    mod = true;
    unsigned rs = (w+ALIGNMENT_ADDITIVE)/ALIGNMENT_FACTOR;
    for(unsigned y = 0; y < h; y++){
        for(unsigned x = 0; x < rs; x++){
            data.flipbyte(y*rs + x);
        }
    }
}
void Bitmap::rotation90() {
    mod = true;

    Bitset tmp(data.size());
    unsigned len = w*h;
    for(unsigned i = 0; i < len; i++){
        if(data.read(i)){
            tmp.set((i/w)+h*(w - 1- i%w));
        }
    }

    unsigned temp = w;
    w = h;
    h = temp;
    data = tmp;
}
void Bitmap::rotation180() {
    unsigned len = w*h;
    for(unsigned i = 0; i < len/2; i++){
        bool tmp = data.read(i);
        if(data.read(len-i)){
            data.set(i);
        }else{
            data.clear(i);
        }
        if(tmp){
            data.set(len-i);
        }else{
            data.clear(len-i);
        }
    }

    mod = true;
}
void Bitmap::rotation270() {
    mod = true;
    Bitset tmp(data.size());
    unsigned len = w*h;
    for(unsigned i = 0; i < len; i++){
        if(data.read(i)){
            tmp.set((h-1-i/w)+h*(i%w));
        }
    }

    unsigned temp = w;
    w = h;
    h = temp;
    data = tmp;
}

AbstractMap* Bitmap::clone() const {
    return new Bitmap(*this);
}