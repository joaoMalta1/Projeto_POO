package model;

class CompleteJavaInfo {
    static void check() {
        System.out.println("=== RUNTIME INFORMATION ===");
        System.out.println("Java Version: " + System.getProperty("java.version"));
        System.out.println("Java Home: " + System.getProperty("java.home"));
        System.out.println("Java Vendor: " + System.getProperty("java.vendor"));
        System.out.println("JVM Name: " + System.getProperty("java.vm.name"));
        System.out.println("JVM Version: " + System.getProperty("java.vm.version"));
        System.out.println("JVM Vendor: " + System.getProperty("java.vm.vendor"));
        
        System.out.println("\n=== RUNTIME VERSION ===");
        Runtime.Version runtimeVersion = Runtime.version();
        System.out.println("Feature: " + runtimeVersion.feature());
        System.out.println("Interim: " + runtimeVersion.interim());
        System.out.println("Update: " + runtimeVersion.update());
        System.out.println("Patch: " + runtimeVersion.patch());
        System.out.println("Build: " + runtimeVersion.build());
        System.out.println("Pre: " + runtimeVersion.pre());
        System.out.println("Full: " + runtimeVersion.toString());
        
        System.out.println("\n=== SYSTEM INFORMATION ===");
        System.out.println("OS: " + System.getProperty("os.name"));
        System.out.println("OS Version: " + System.getProperty("os.version"));
        System.out.println("Architecture: " + System.getProperty("os.arch"));
    }
}
