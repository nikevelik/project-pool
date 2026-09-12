#pragma once
#include "String.h"
String getFileType(const String& filename) {
    for (int i = filename.getSize() - 1; i >= 0; --i) {
        if (filename[i] == '.') {
            return &filename[i+1];
            break;
        }
    }
    throw std::runtime_error("invalid filename passed");

}

bool isValidFileType(const String& filename){
    try{
        String format = getFileType(filename);
        return format=="ppm" || format =="pgm" || format == "pbm";
    }catch(...){
        return false;
    }
}