#pragma once
#include <utility>
#include <fstream>
#include "MagicValue.h"
#include "../assets/String.h"
#include "../assets/Bitset.h"

template <class T>
class Map;

template <class T>
class GraphicMatrix{
public:
    GraphicMatrix() = default;
    GraphicMatrix(unsigned width, unsigned height, unsigned maxvalue);
    ~GraphicMatrix();

    GraphicMatrix(const GraphicMatrix<T>& o);
    GraphicMatrix(GraphicMatrix<T>&& o) noexcept;
    GraphicMatrix<T>& operator=(const GraphicMatrix<T>& o);
    GraphicMatrix<T>& operator=(GraphicMatrix<T>&& o) noexcept;

    unsigned width() const;
    unsigned height() const;
    unsigned maxvalue() const;
    bool isCorrect() const;


    void transpose();
    void rotation90();
    void rotation180();
    void rotation270();

    void grayscale();
    void monochrome();
    void negative();

    const T* operator[](unsigned idx) const;
    T* operator[](unsigned idx);
private:
    T** data = nullptr;
    unsigned w = 0;
    unsigned h = 0;
    unsigned mv = 0;

    void init(unsigned width, unsigned height, unsigned maxvalue);
    void destroy();
    void copy(const GraphicMatrix<T>& o);
    void move(GraphicMatrix<T>&& o);



    friend class Map<T>;
    GraphicMatrix(std::ifstream& infile);
    void deserializeraw(std::ifstream& infile);
    void deserializeplain(std::ifstream& infile);
    void serializeraw(const String& filename) const;
    void serializeplain(const String& filename) const;
};
template <class T>
void GraphicMatrix<T>::copy(const GraphicMatrix<T>& o){
    w = o.w;
    h = o.h;
    mv = o.mv;
    data = new T*[h];
    for(unsigned y = 0; y < h; y++){
        data[y] = new T[w];
        for(unsigned x = 0; x < w; x++){
            data[y][x] = o.data[y][x];   
        }
    }
}
template <class T>
void GraphicMatrix<T>::move(GraphicMatrix<T>&& o){
    w = o.w;
    o.w = 0;
    h = o.h;
    o.h = 0;
    mv = o.mv;
    o.mv = 0;
    data = o.data;
    o.data = nullptr;
}
template <class T>
void GraphicMatrix<T>::destroy(){
    if(data){
        for(unsigned y = 0; y<h; y++){
            delete[] data[y];
        }
        delete[] data;
        data = nullptr;
    }
}

template <class T>
GraphicMatrix<T>::GraphicMatrix(unsigned width, unsigned height, unsigned maxvalue){
    init(width, height, maxvalue);
}
template <class T>
void GraphicMatrix<T>::init(unsigned width, unsigned height, unsigned maxvalue){
    w = width;
    h = height;
    mv = maxvalue;
    data = new T*[h];
    for(unsigned y = 0; y < h; y++){
        data[y] = new T[w];
    }
}

template <class T>
GraphicMatrix<T>::~GraphicMatrix(){
    destroy();
}
template <class T>
GraphicMatrix<T>::GraphicMatrix(const GraphicMatrix<T>& o){
    copy(o);
}
template <class T>
GraphicMatrix<T>::GraphicMatrix(GraphicMatrix<T>&& o) noexcept{
    move(std::move(o));
}
template <class T>
GraphicMatrix<T>& GraphicMatrix<T>::operator=(const GraphicMatrix<T>& o){
    if(this != &o){
        destroy();
        copy(o);
    }
    return *this;
}
template <class T>
GraphicMatrix<T>& GraphicMatrix<T>::operator=(GraphicMatrix<T>&& o) noexcept{
    if(this != &o){
        destroy();
        move(std::move(o));
    }
    return *this;
}
template <class T>
unsigned GraphicMatrix<T>::width() const{
    return w;
}
template <class T>
unsigned GraphicMatrix<T>::height() const{
    return h;
}
template <class T>
unsigned GraphicMatrix<T>::maxvalue() const{
    return mv;
}
template <class T>
bool GraphicMatrix<T>::isCorrect() const{
    for(unsigned y = 0; y < h; y++){
        for(unsigned x = 0; x < w; x++){
            if(!data[y][x].isCorrect(mv)){
                return false;
            }
        }
    }
    return true;
}
template <class T>
const T* GraphicMatrix<T>::operator[](unsigned idx) const{
    return data[idx];
}
template <class T>
T* GraphicMatrix<T>::operator[](unsigned idx){
    return data[idx];
}
template <class T>
void GraphicMatrix<T>::rotation90(){
    GraphicMatrix<T> temp(h, w, mv);
    for(unsigned y = 0; y < h; y++){
        for(unsigned x = 0; x < w; x++){
            temp[x][y] = data[h-1-y][x];
        }
    }
    *this = std::move(temp);
}
template <class T>
void GraphicMatrix<T>::rotation270(){
    GraphicMatrix<T> temp(h, w, mv);
    for(unsigned y = 0; y < h; y++){
        for(unsigned x = 0; x < w; x++){
            temp[x][y] = data[y][w - 1 - x];
        }
    }
    *this = std::move(temp);
}
template <class T>
void GraphicMatrix<T>::rotation180(){
    unsigned midh = h/2, midw = w/2;
    for(unsigned y = 0; y < midh; y++){
        T* temp = data[y];
        data[y] = data[h-1-y];
        data[h-1-y] = temp;
        for(unsigned x = 0; x < midw; x++){
            T temp = data[y][x];
            data[y][x] = data[y][w-1-x];
            data[y][w-1-x] = temp;
            temp = data[h-1-y][x];
            data[h-1-y][x] = data[h-1-y][w-1-x];
            data[h-1-y][w-1-x] = temp;
        }
    }
    if(h%2==1){
        for(unsigned x = 0; x < midw; x++){
            T temp = data[midh][x];
            data[midh][x] = data[midh][w-1-x];
            data[midh][w-1-x] = temp;
        }
    }
}
template <class T>
void GraphicMatrix<T>::grayscale(){
    for(unsigned y = 0; y < h; y++){
        for(unsigned x = 0; x < w; x++){
            data[y][x].grayscale();
        }
    }
}
template <class T>
void GraphicMatrix<T>::monochrome(){
    for(unsigned y = 0; y < h; y++){
        for(unsigned x = 0; x < w; x++){
            data[y][x].monochrome(mv);
        }
    }
}
template <class T>
void GraphicMatrix<T>::negative(){
    for(unsigned y = 0; y < h; y++){
        for(unsigned x = 0; x < w; x++){
            data[y][x].negative(mv);
        }
    }
}
template <class T>
void GraphicMatrix<T>::deserializeraw(std::ifstream& infile){
    for (unsigned y = 0; y < h; ++y) {
        infile.read(reinterpret_cast<char*>(data[y]), sizeof(data[0][0]) * w); 
        if(!infile.good()){
            throw std::runtime_error("Binary map is damaged. Could not read from the file");
        }
    }
    if(!isCorrect()){
        throw std::runtime_error("Error due pixel outside the specified boundaries");
    }
}
template <class T>
void GraphicMatrix<T>::deserializeplain(std::ifstream& infile){
    for (unsigned y = 0; y < h; y++) { 
        for (unsigned x = 0; x < w; x++) {  
            infile >> data[y][x];
            if(!data[y][x].isCorrect(mv)){
                throw std::runtime_error("Error due value outside the specified boundaries");
            }
        }
    }
}
template <class T>
void GraphicMatrix<T>::serializeraw(const String& filename) const{
    std::ofstream outfile(filename.c_str(), std::ios::binary);
    if (!outfile) {
        throw std::runtime_error("Error: Could not open file for writing.");
    }
    outfile << MagicValue<T>::raw() << "\n";
    outfile << w << " " << h << "\n";
    outfile << mv << "\n";
    for (unsigned y = 0; y < h; ++y) {
        outfile.write(reinterpret_cast<const char*>(data[y]), sizeof(data[0][0])*w);
    }
}
template <class T>
void GraphicMatrix<T>::serializeplain(const String& filename) const{
    std::ofstream outfile(filename.c_str());
    if (!outfile) {
        throw std::runtime_error("Error: Could not open file for writing");
    }
    outfile << MagicValue<T>::plain() + "\n";
    outfile << w << " " << h << "\n";
    outfile << mv << "\n";
    for (unsigned y = 0; y < h; y++) {
        for (unsigned x = 0; x < w; x++) {
            outfile << data[y][x];
        }
        outfile << "\n";
    }
}
template<class T>
GraphicMatrix<T>::GraphicMatrix(std::ifstream& infile){
    unsigned w, h, mv;
    infile >> w >> h >> mv;
    if(!infile.good()){
        throw std::runtime_error("Invalid header values in file");
    }
    if(mv > T::MAX_ALLOWED){
        throw std::runtime_error("This application cannot handle such boundary values");
    }
    init(w, h, mv);
    char newline;
    infile.get(newline);
    if(!infile.good() || newline != '\n'){
        throw std::runtime_error("No separation between header and binary data. File is invalid");
    }
}

template <>
void GraphicMatrix<Shade>::grayscale(){
    return;
}