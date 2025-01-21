#include "../include/ConnectionHandler.h"
#include "../include/StompProtocol.h"
#include <iostream>
#include <thread>
#include <atomic>
   
int main(int argc, char **argv) {
    StompProtocol protocol;
    ConnectionHandler *ch = nullptr;
    std::thread serverThread;
    std::atomic_bool stopReading(false);
    bool isLoggedIn = false;

    while (true) {
        std::string line;
        if (!std::getline(std::cin, line)) {
            break;
        }

        std::vector<std::string> tokens = protocol.split(line, ' ');
        if (tokens.empty()) {
            continue;
        }
        std::string cmd = tokens[0];

        if (cmd == "login") {
            if (isLoggedIn) {
                std::cout << "Already logged in. Please logout first.\n";
                continue;
            }
            if (tokens.size() < 4) {
                std::cout << "Usage: login {host:port} {username} {password}\n";
                continue;
            }
            std::string hostport = tokens[1];
            std::string host = hostport.substr(0, hostport.find(':'));
            short port = std::stoi(hostport.substr(hostport.find(':') + 1));
            std::string user = tokens[2];
            std::string pass = tokens[3];

            if (ch != nullptr) {
                delete ch;
                ch = nullptr;
            }
            ch = new ConnectionHandler(host, port);

            if (!ch->connect()) {
                std::cout << "Could not connect to server\n";
                delete ch;
                ch = nullptr;
                continue;
            }
//             std::string connectFrame = protocol.buildConnectFrame(hostport, user, pass);
//             ch->sendFrameAscii(connectFrame, '\0');

//             stopReading = false;
//             serverThread = std::thread(serverReader, ch, &protocol, &stopReading);

//             try {
//                 std::string response = protocol.waitForResponse();
//                 if (response.find("CONNECTED") != std::string::npos) {
//                     isLoggedIn = true;
//                     std::cout << "Login successful.\n";
//                 }
//             } catch (const std::runtime_error& e) {
//                 std::cerr << "Login failed: " << e.what() << "\n";
//                 delete ch;
//                 ch = nullptr;
//                 stopReading = true;
//                 break;
//             }
//         } else {
//             if (!protocol.handleKeyboardInput(line, ch, serverThread, stopReading, isLoggedIn)) {
//                 break;
//             }
//         }
//     }

//     stopReading = true;
//     if (serverThread.joinable()) {
//         serverThread.join();
//     }
//     if (ch != nullptr) {
//         ch->close();
//         delete ch;
//         ch = nullptr;
//     }

//     std::cout << "Client finished.\n";
//     return 0;
// }
}
    }
