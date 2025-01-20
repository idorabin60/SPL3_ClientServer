// Frame.cpp
#include "frame.h"
#include <sstream>
#include <stdexcept>

// Constructors
Frame::Frame() : command(""), headers(), body(""){}

Frame::Frame(const std::string& command, const std::map<std::string, std::string>& headers, const std::string& body)
    : command(command), headers(headers), body(body) {}

// Getters
const std::string& Frame::getCommand() const {
    return command;
}

const std::map<std::string, std::string>& Frame::getHeaders() const {
    return headers;
}

const std::string& Frame::getBody() const {
    return body;
}

// Setters
void Frame::setCommand(const std::string& command) {
    this->command = command;
}

void Frame::setHeaders(const std::map<std::string, std::string>& headers) {
    this->headers = headers;
}

void Frame::addHeader(const std::string& key, const std::string& value) {
    headers[key] = value;
}

void Frame::setBody(const std::string& body) {
    this->body = body;
}

//return the string of the frame 
std::string Frame::serialize() const {
    std::ostringstream oss;
    oss << command << "\n";
    for (const auto& header : headers) {
        oss << header.first << ":" << header.second << "\n";
    }
    oss << "\n" << body << "\0";
    return oss.str();
}

// Frame Frame::deserialize(const std::string& frameString) {
//     std::istringstream iss(frameString);
//     std::string line;

//     // Parse command
//     if (!std::getline(iss, line) || line.empty()) {
//         throw std::invalid_argument("Invalid frame: Missing command");
//     }
//     std::string command = line;

//     // Parse headers
//     std::map<std::string, std::string> headers;
//     while (std::getline(iss, line) && !line.empty()) {
//         size_t colonPos = line.find(":");
//         if (colonPos == std::string::npos) {
//             throw std::invalid_argument("Invalid frame: Malformed header");
//         }
//         std::string key = line.substr(0, colonPos);
//         std::string value = line.substr(colonPos + 1);
//         headers[key] = value;
//     }

//     // Parse body
//     std::string body;
//     if (std::getline(iss, line, '\0')) {
//         body = line;
//     }

//     return Frame(command, headers, body);
// }

