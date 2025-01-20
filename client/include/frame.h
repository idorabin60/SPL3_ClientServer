// Frame.h
#ifndef FRAME_H
#define FRAME_H

#include <string>
#include <map>

class Frame {
public:
    // Constructors
    Frame();
    Frame(const std::string& command, const std::map<std::string, std::string>& headers, const std::string& body);

    // Getters
    const std::string& getCommand() const;
    const std::map<std::string, std::string>& getHeaders() const;
    const std::string& getBody() const;

    // Setters
    void setCommand(const std::string& command);
    void setHeaders(const std::map<std::string, std::string>& headers);
    void addHeader(const std::string& key, const std::string& value);
    void setBody(const std::string& body);

    // Serialization and Deserialization
    std::string serialize() const;

private:
    std::string command;
    std::map<std::string, std::string> headers;
    std::string body;

};

#endif // FRAME_H
