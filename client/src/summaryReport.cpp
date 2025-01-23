#include "summaryReport.h"
#include <filesystem>

// Constructor
summaryReport::summaryReport(const std::string& channelName)
    : channelName(channelName), totalReports(0), activeCount(0), forcesArrivalAtSceneCount(0) {}

// Setters and Getters
void summaryReport::setChannelName(const std::string& name) {
    channelName = name;
}

std::string summaryReport::getChannelName() const {
    return channelName;
}

void summaryReport::setTotalReports(int total) {
    totalReports = total;
}

int summaryReport::getTotalReports() const {
    return totalReports;
}

void summaryReport::setActiveCount(int active) {
    activeCount = active;
}

int summaryReport::getActiveCount() const {
    return activeCount;
}

void summaryReport::setForcesArrivalAtSceneCount(int count) {
    forcesArrivalAtSceneCount = count;
}

int summaryReport::getForcesArrivalAtSceneCount() const {
    return forcesArrivalAtSceneCount;
}

// Event Reports Queue Management
void summaryReport::addEventReport(const Event& report) {
    eventReportsQueue.push(report);
    totalReports++;
}

void summaryReport::removeEventReport() {
    if (!eventReportsQueue.empty()) {
        eventReportsQueue.pop();
    }
}

bool summaryReport::isEventQueueEmpty() const {
    return eventReportsQueue.empty();
}

const std::queue<Event>& summaryReport::getEventReports() const {
    return eventReportsQueue;
}

// Debugging and Display
void summaryReport::printSummary() const {
    std::cout << "Channel: " << channelName << "\n";
    std::cout << "Total Reports: " << totalReports << "\n";
    std::cout << "Active Reports: " << activeCount << "\n";
    std::cout << "Forces Arrival at Scene: " << forcesArrivalAtSceneCount << "\n";

    std::queue<Event> tempQueue = eventReportsQueue; // Create a copy to iterate
    int reportNumber = 1;

    while (!tempQueue.empty()) {
        const Event& report = tempQueue.front();
        std::cout << "Report_" << reportNumber << ":\n";
        std::cout << "  City: " << report.get_city() << "\n";
        std::cout << "  Date/Time: " << report.get_date_time() << "\n";
        std::cout << "  Event Name: " << report.get_name() << "\n";
        std::cout << "  Description: " << report.get_description() << "\n";
        tempQueue.pop();
        reportNumber++;
    }
}

