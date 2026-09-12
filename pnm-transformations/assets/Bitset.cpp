#include "Bitset.h"
#include <utility>
Bitset::Bitset(unsigned size){
	n = size;
	data = new char[n]{0};
}

void Bitset::copy(const Bitset& o){
	n = o.n;
	data = new char[n];
	for(int i = 0; i < n; i++){
		data[i] = o.data[i];
	}
}

void Bitset::move(Bitset&& o){
	n = o.n;
	o.n = 0;
	data = o.data;
	o.data = nullptr;
}

void Bitset::destroy(){
	n = 0;
	delete[] data;
	data = nullptr;
}

Bitset::~Bitset(){
	destroy();
}

Bitset& Bitset::operator=(const Bitset& o){
	if(this != &o){
		destroy();
		copy(o);
	}
	return *this;
}

Bitset&  Bitset::operator=(Bitset&& o) noexcept {
	if(this != &o){
		destroy();
		move(std::move(o));
	}
	return *this;
}

Bitset::Bitset(const Bitset& o){
	copy(o);
}

Bitset::Bitset(Bitset&& o) noexcept {
	move(std::move(o));
}

unsigned Bitset::byteof(unsigned key){
    return key / BITS_IN_BYTE;
}

unsigned Bitset::maskfor(unsigned key){
    return 1 << (BITS_IN_BYTE - key % BITS_IN_BYTE - 1);
}

void Bitset::set(unsigned key){
    data[byteof(key)] |= maskfor(key);
}

void Bitset::clear(unsigned key){
    data[byteof(key)] &= ~maskfor(key);
}

bool Bitset::read(unsigned key) const {
    return (bool) (maskfor(key) & data[byteof(key)]);
}

char* Bitset::ptr(){
	return data;
}

const char* Bitset::ptr() const {
	return data;
}

void Bitset::flipbyte(unsigned idx){
	data[idx] = ~data[idx];
}

unsigned Bitset::size() const{
	return n;
}