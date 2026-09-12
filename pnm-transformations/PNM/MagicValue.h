#pragma once
#include "../assets/String.h"

template <class T>
struct MagicValue;


template <>
struct MagicValue<class Bit> {
    static const String plain() { return "P1"; }
    static const String raw() { return "P4"; }
};

template <>
struct MagicValue<class Shade> {
    static const String plain() { return "P2"; }
    static const String raw() { return "P5"; }
};

template <>
struct MagicValue<class Pixel> {    
    static const String plain() { return "P3"; }
    static const String raw() { return "P6"; }
};

