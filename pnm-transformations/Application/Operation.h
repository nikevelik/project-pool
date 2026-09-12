#pragma once
enum class Operation {
    Grayscale,
    Monochrome,
    Negative,
    RotationL,
    RotationR
};

std::ostream& operator<<(std::ostream& os, const Operation& op) {
    switch (op) {
        case Operation::Grayscale:
            os << "Grayscale";
            break;
        case Operation::Monochrome:
            os << "Monochrome";
            break;
        case Operation::Negative:
            os << "Negative";
            break;
        case Operation::RotationL:
            os << "RotationL";
            break;
        case Operation::RotationR:
            os << "RotationR";
            break;
    }
    return os;
}