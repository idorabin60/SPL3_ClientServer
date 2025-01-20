#pragma once
#include "event.h"
#include <string>
#include <queue>
#include <iostream>

class summaryReport {
private:
    std::string channelName;                      // Name of the channel
    int totalReports;                             // Total number of reports
    int activeCount;                              // Count of 'true' active reports
    int forcesArrivalAtSceneCount;               // Count of 'true' forces arrival at scene
    std::queue<Event> eventReportsQueue;         // Queue of Event objects

public:
    // Constructor
    summaryReport(const std::string& channelName);

    // Destructor
    ~summaryReport() = default; // No dynamic memory, so default destructor is sufficient

    // Setters and Getters
    void setChannelName(const std::string& name);
    std::string getChannelName() const;

    void setTotalReports(int total);
    int getTotalReports() const;

    void setActiveCount(int active);
    int getActiveCount() const;

    void setForcesArrivalAtSceneCount(int count);
    int getForcesArrivalAtSceneCount() const;

    // Event Reports Queue Management
    void addEventReport(const Event& report);
    void removeEventReport(); // Removes the oldest event report
    bool isEventQueueEmpty() const;
    const std::queue<Event>& getEventReports() const;

    // Debugging and Display
    void printSummary() const;
};
