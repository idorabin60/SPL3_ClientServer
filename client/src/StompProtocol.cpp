#include "StompProtocol.h"
#include <iostream>
#include <stdexcept>

// Constructor and Destructor
StompProtocol::StompProtocol()
    : nextSubscribeID(0), receiptId(0) {}

StompProtocol::~StompProtocol() {}

// Subscription Management
int StompProtocol::generateSubscribeID() {
    return ++nextSubscribeID;
}
// Receipt ID Management
int StompProtocol::generateReceiptId() {
    return ++receiptId;
}

void StompProtocol::addChannel(int idSubscription, const std::string& channel) {
    mapChannelID[channel] = idSubscription;
}

void StompProtocol::deleteSubscriptionChannel(int id) {
    for (auto it = mapChannelID.begin(); it != mapChannelID.end(); ++it) {
        if (it->second == id) {
            mapChannelID.erase(it);
            return;
        }
    }
    std::cerr << "Subscription ID " << id << " not found." << std::endl;
}

std::string StompProtocol::getChannelById(int id) const {
    for (const auto& pair : mapChannelID) {
        if (pair.second == id) {
            return pair.first;
        }
    }
    return ""; // Return an empty string if the channel is not found
}

int StompProtocol::getSubscriptionIDByName(const std::string& channelName) {
    auto it = mapChannelID.find(channelName);
    if (it != mapChannelID.end()) {
        return it->second;
    }
    return -1; // Return -1 if the channel name is not found
}

void StompProtocol::printChannels() const {
    for (const auto& pair : mapChannelID) {
        std::cout << "Channel: " << pair.first << ", Subscription ID: " << pair.second << std::endl;
    }
}

// Reports Management
void StompProtocol::addReport(const std::string& channel, const std::string& user, const std::string& report) {
    std::lock_guard<std::mutex> lock(reportsMutex);
    reports[channel][user].push_back(report);
}

void StompProtocol::deleteChannel(const std::string& channel) {
    std::lock_guard<std::mutex> lock(reportsMutex);
    auto it = reports.find(channel);
    if (it != reports.end()) {
        reports.erase(it);
        std::cout << "Channel \"" << channel << "\" deleted successfully." << std::endl;
    } else {
        std::cout << "Channel \"" << channel << "\" does not exist." << std::endl;
    }
}

std::vector<std::string> StompProtocol::getReportsFromUser(const std::string& channel, const std::string& user) const {
    std::lock_guard<std::mutex> lock(reportsMutex);
    auto channelIt = reports.find(channel);
    if (channelIt != reports.end()) {
        auto userIt = channelIt->second.find(user);
        if (userIt != channelIt->second.end()) {
            return userIt->second;
        }
    }
    return {}; // Return an empty vector if no reports are found
}




