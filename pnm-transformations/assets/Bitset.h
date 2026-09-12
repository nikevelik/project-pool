#pragma once
class Bitset{
	public: 
		Bitset() = default;
		Bitset(unsigned size);
		~Bitset();
		Bitset(const Bitset& o);
		Bitset(Bitset&& o) noexcept;
		Bitset& operator=(const Bitset& o);
		Bitset& operator=(Bitset&& o) noexcept;
		void set(unsigned key);
		void clear(unsigned key);
		bool read(unsigned key) const;
		
		char* ptr();
		const char* ptr() const ;
		void flipbyte(unsigned idx);
		unsigned size() const;
	private:
		static const unsigned BITS_IN_BYTE = 8;
		unsigned n = 0;  
		char* data = nullptr;
		static unsigned byteof(unsigned key);
		static unsigned maskfor(unsigned key);
		void copy(const Bitset& o);
		void move(Bitset&& o);
		void destroy();
}; 