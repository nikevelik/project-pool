#pragma once
class Pixel {
public:
    static unsigned MAX_ALLOWED;
    bool isCorrect(unsigned char maxvalue) const;
    bool isGray() const;
    bool isMonochrome(unsigned char maxvalue) const;
    void grayscale();
    void monochrome(unsigned char maxvalue);
    void negative(unsigned char maxvalue);
    static void check(std::ifstream& ifs, int v);
    friend std::ifstream& operator>>(std::ifstream& ifs, Pixel& obj);
    friend std::ofstream& operator<<(std::ofstream& ofs,const Pixel& obj);
private:
    unsigned char r=0, g=0, b=0;
};
