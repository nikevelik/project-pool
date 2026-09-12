#pragma once
#include "AbstractMap.h"
#include "../assets/String.h"
#include "GraphicMatrix.hpp"
#include <fstream>
#include <stdexcept>
#include "Shade.h"
#include "Grayness.h"
#include "Pixel.h"

template<class T>
class Map : public AbstractMap{
    public:
        void serializeraw(const String& filename) const override;
        void serializeplain(const String& filename) const override;
        void grayscale() override;
        void monochrome() override;
        void negative() override;
        void rotation90() override;
        void rotation180() override;
        void rotation270() override;
        AbstractMap* clone() const override;

        Map(const String& filename);
        
    private:
        void deserializeplain(std::ifstream& infile) override;
        void deserializeraw(std::ifstream& infile) override;
        void deserializeheader(std::ifstream& infile) override;

        GraphicMatrix<T> data;
};

template <class T>
void Map<T>::deserializeheader(std::ifstream& infile){
    String magic;
    infile >> magic;
    if (magic == MagicValue<T>::plain()) {
        raw = false;
    }else if(magic == MagicValue<T>::raw()){
        raw = true;
    }else{
        throw std::runtime_error("Invalid magic number for Map files");
    }
    data = GraphicMatrix<T>(infile);
}
template <class T>
void Map<T>::deserializeplain(std::ifstream& infile) {
    data.deserializeplain(infile);
}
template <class T>
void Map<T>::deserializeraw(std::ifstream& infile) {
    data.deserializeraw(infile);    
}
template <class T>
Map<T>::Map(const String& filename){
    deserialize(filename);
}
template <class T>
void Map<T>::serializeplain(const String& filename) const {
    data.serializeplain(filename);
}
template <class T>
void Map<T>::serializeraw(const String& filename) const {
    data.serializeraw(filename);
}
template <class T>
void Map<T>::rotation90(){
    mod = true;
    data.rotation90();
}
template <class T>
void Map<T>::rotation270(){
    mod = true;
    data.rotation270();
}
template <class T>
void Map<T>::rotation180(){
    mod = true;
    data.rotation180();
}
template <class T>
void Map<T>::grayscale(){
    if(!isGray()){
        g = Grayness::Grayscale;
        mod = true;
        data.grayscale();
    }
}
template <class T>
void Map<T>::monochrome(){
    if(!isMonochrome()){
        g = Grayness::Monochrome;
        mod = true;
        data.monochrome();
    }
}
template <class T>
void Map<T>::negative(){
    mod = true;
    data.negative();
}

template <class T>
AbstractMap* Map<T>::clone() const {
    return new Map<T>(*this);
}


template <>
Map<Shade>::Map(const String& filename){
    g = Grayness::Grayscale;
    deserialize(filename);
}


using Pixmap = Map<Pixel>;
using Graymap = Map<Shade>;