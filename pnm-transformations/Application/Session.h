#pragma once
#include "../assets/String.h"
#include "../assets/Container.hpp"
#include "../assets/Vector.hpp"
#include "../assets/Pair.hpp"
#include "Operation.h"
#include "../PNM/Grayness.h"
#include "../PNM/AbstractMap.h"
#include "../PNM/PNMFactory.h"
#include "../assets/fileformats.h"
class Session{
    public:
        Session() = default;
        Session(const String& filename);
        void save() noexcept;
        void saveas(const String& filename) const noexcept;
        void info() const noexcept;
        void add(const String& filename) noexcept;
        void undo() noexcept;
        void collage(const String& i1, const String& i2, bool isVertical, const String& out) noexcept;
        void operate(const Operation& op) noexcept;
        unsigned getID() const noexcept;
    private:
        unsigned id = 0;
        static unsigned lastid;
        static void calc(const Operation& op, int& r, Grayness& g, bool& n) noexcept;
        static void apply(AbstractMap* target, int r, Grayness g, bool n) noexcept;
        Container<AbstractMap, PNMFactory> img;
        Vector<Pair<String, unsigned>> files;
        Vector<Operation> operations;
};
