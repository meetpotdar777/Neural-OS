// Go: Conceptual Network Service (e.g., for internal OS messaging)
// This code is NOT runnable in a browser and requires a Go compiler.
// It simulates a web server handling requests concurrently.

package main

import (
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"time"
)

// Event represents a conceptual message or event in the OS
type Event struct {
	Type    string `json:"type"`
	Payload string `json:"payload"`
	Timestamp time.Time `json:"timestamp"`
}

// In-memory channel to simulate an event queue for concurrent processing
var eventQueue = make(chan Event, 100)

func handleSystemEvent(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "Only POST method is supported for system events.", http.StatusMethodNotAllowed)
		return
	}

	var event Event
	err := json.NewDecoder(r.Body).Decode(&event)
	if err != nil {
		http.Error(w, "Invalid JSON payload.", http.StatusBadRequest)
		return
	}
	event.Timestamp = time.Now()

	select {
	case eventQueue <- event:
		fmt.Fprintf(w, "Event '%s' received and queued for processing.", event.Type)
		log.Printf("[Go Network Service]: Received event: %+v", event)
	default:
		http.Error(w, "Event queue is full, try again later.", http.StatusServiceUnavailable)
		log.Println("[Go Network Service]: Event queue full.")
	}
}

// goroutine to process events from the queue concurrently
func processEvents() {
	for event := range eventQueue {
		log.Printf("[Go Network Service]: Processing event Type: %s, Payload: %s", event.Type, event.Payload)
		// Simulate some work or forwarding to another service
		time.Sleep(50 * time.Millisecond) // Simulate lightweight work
		log.Printf("[Go Network Service]: Finished processing event Type: %s", event.Type)
	}
}

func main() {
	log.Println("Go Network Service: Starting...")

	// Start a goroutine to process events asynchronously
	go processEvents()

	// Define HTTP endpoint
	http.HandleFunc("/system_event", handleSystemEvent)

	port := ":8081" // Example port for internal OS service
	log.Printf("Go Network Service: Listening on %s", port)
	// In a real OS, this would be an internal network listener, likely secured.
	log.Fatal(http.ListenAndServe(port, nil))
}

/*
To conceptually interact with this (if it were running locally as a separate process):
You would send an HTTP POST request, e.g., using curl:
curl -X POST -H "Content-Type: application/json" -d '{"type":"UI_CLICK", "payload":"settings_button_pressed"}' http://localhost:8081/system_event
*/
