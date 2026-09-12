#pragma once
#include "../assets/String.h"
#include "AbstractMap.h"
#include "MagicValue.h"

class Bitmap : public AbstractMap{
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

        Bitmap(const String& filename);
        
    private:
        void deserializeplain(std::ifstream& infile) override;
        void deserializeraw(std::ifstream& infile) override;
        void deserializeheader(std::ifstream& infile) override;
        unsigned w = 0;
        unsigned h = 0;
        Bitset data;
};
