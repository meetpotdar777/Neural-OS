// File name: ApplicationManager.java
// Java: Conceptual Application Manager Service
// This code is NOT runnable in a browser and requires a Java Development Kit (JDK) to compile and run.
// It simulates managing installed and running applications.

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap; // For thread-safe map

public class ApplicationManager {

    // Simulates a registry of installed applications
    private static final Map<String, String> INSTALLED_APPS = new ConcurrentHashMap<>();
    // Simulates a registry of running application processes
    private static final Map<String, String> RUNNING_PROCESSES = new ConcurrentHashMap<>(); // Process ID -> App Name

    public ApplicationManager() {
        System.out.println("[Java App Manager]: Initializing...");
        // Pre-populate some installed apps for demonstration
        INSTALLED_APPS.put("browser_app", "NeuralBrowser");
        INSTALLED_APPS.put("email_app", "NeuralMail");
        INSTALLED_APPS.put("photos_app", "NeuralPhotos");
    }

    /**
     * Simulates the installation of an application.
     * @param appId A unique identifier for the app.
     * @param appName The display name of the app.
     * @return A status message.
     */
    public String installApplication(String appId, String appName) {
        if (INSTALLED_APPS.containsKey(appId)) {
            return "Application " + appName + " is already installed.";
        }
        INSTALLED_APPS.put(appId, appName);
        System.out.println("[Java App Manager]: Installed app: " + appName + " (" + appId + ")");
        return "Application " + appName + " installed successfully.";
    }

    /**
     * Simulates launching an application.
     * @param appId The ID of the application to launch.
     * @return A unique process ID for the launched app, or null if app not found/failed to launch.
     */
    public String launchApplication(String appId) {
        if (!INSTALLED_APPS.containsKey(appId)) {
            return null; // App not installed
        }
        String appName = INSTALLED_APPS.get(appId);
        String processId = "proc-" + UUID.randomUUID().toString(); // Generate unique process ID
        RUNNING_PROCESSES.put(processId, appName);
        System.out.println("[Java App Manager]: Launched app: " + appName + " (Process ID: " + processId + ")");
        return processId;
    }

    /**
     * Simulates terminating a running application.
     * @param processId The process ID of the app to terminate.
     * @return A status message.
     */
    public String terminateApplication(String processId) {
        if (RUNNING_PROCESSES.containsKey(processId)) {
            String appName = RUNNING_PROCESSES.remove(processId);
            System.out.println("[Java App Manager]: Terminated app: " + appName + " (Process ID: " + processId + ")");
            return "Application " + appName + " terminated.";
        }
        return "Process " + processId + " not found or not running.";
    }

    public void listInstalledApps() {
        System.out.println("\n[Java App Manager]: Installed Applications:");
        INSTALLED_APPS.forEach((id, name) -> System.out.println(" - " + name + " (ID: " + id + ")"));
    }

    public void listRunningProcesses() {
        System.out.println("\n[Java App Manager]: Running Processes:");
        RUNNING_PROCESSES.forEach((id, name) -> System.out.println(" - " + name + " (Process ID: " + id + ")"));
    }


    public static void main(String[] args) {
        ApplicationManager appManager = new ApplicationManager();

        appManager.listInstalledApps();

        String browserProcId = appManager.launchApplication("browser_app");
        String emailProcId = appManager.launchApplication("email_app");

        appManager.listRunningProcesses();

        System.out.println(appManager.installApplication("new_game_app", "NeuralGame"));
        System.out.println(appManager.installApplication("browser_app", "NeuralBrowser")); // Already installed

        System.out.println(appManager.terminateApplication(browserProcId));
        System.out.println(appManager.terminateApplication("nonexistent-proc-id"));

        appManager.listRunningProcesses();
    }
}
