#pragma once
#include "../include/ConnectionHandler.h"
#include "frame.h"
#include "summaryReport.h"
#include "StompProtocol.h"
#include <unordered_map>
#include <map>
#include <vector>
#include <string>
#include <memory>
#include <mutex>

class StompProtocol {
private:
    int nextSubscribeID;
    int receiptId;
    std::unordered_map<std::string, int> mapChannelID;
    std::map<std::string, std::map<std::string, summaryReport>> reports;
    mutable std::mutex reportsMutex;

public:
    // Constructor and Destructor
    StompProtocol();
    ~StompProtocol();

    // Subscription Management
    int generateSubscribeID();
    int generateReceiptId();
    void addChannel(int idSubscription, const std::string& channel);
    void deleteSubscriptionChannel(int id);
    std::string getChannelById(int id) const;
    int getSubscriptionIDByName(const std::string& channelName);
    std::vector<std::string> split(const std::string& str, char delimiter) const;

    // Reports Management
    void addReport(const std::string& channel, const std::string& user, const std::string& report);
    void deleteChannel(const std::string& channel);
    std::vector<std::string> getReportsFromUser(const std::string& channel, const std::string& user) const;

    // Debugging
    void printChannels() const;
};
