import subprocess
import os
import sys
from datetime import datetime
import time

def run_script(script_path, message, latency):
    start_time = time.time()
    
    print(f"\n{'='*50}")
    print(f"[STEP] {message}")
    print(f"[RUN] Executing: {script_path}")
    print(f"[START] {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")
    print('='*50)
    
    try:
        # Run the script with shell=True for Windows
        process = subprocess.Popen(
            f"python {script_path}",
            stdout=subprocess.PIPE,
            stderr=subprocess.STDOUT,
            shell=True,
            text=True
        )

        # Read output in real-time
        while True:
            # Read line by line
            output = process.stdout.readline()
            if output:
                # Print without extra newlines
                print(output.strip())
            # Check if process has finished
            if output == '' and process.poll() is not None:
                break

        return_code = process.poll()
        execution_time = time.time() - start_time
        
        print(f"[LATENCY PAUSE] {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")
        time.sleep(latency)
        print(f"[LATENCY PAUSE] {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")
        if return_code == 0:
            print(f"\n[SUCCESS] Completed: {message}")
            print(f"[TIME] Execution time: {execution_time:.2f} seconds")
            return True
        else:
            print(f"\n[ERROR] Failed with exit code {return_code}: {message}")
            print(f"[TIME] Execution time: {execution_time:.2f} seconds")
            return False
            
    except Exception as e:
        execution_time = time.time() - start_time
        print(f"\n[ERROR] Failed to execute {script_path}")
        print(f"[ERROR] {str(e)}")
        print(f"[TIME] Execution time: {execution_time:.2f} seconds")
        return False

def main():
    print("\n[START] Beginning Script Execution Sequence")
    print(f"[TIME] {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")
    
    scripts_to_run = [
        ("start_docker_services_db.py", "Starting docker services (acme-db)", 5)
        ,("run_acme_db_maven.py", "Running database creation", 1)
        ,("build_acme_api.py", "Building acme-api project", 1)
        ,("start_docker_services_api.py", "Starting docker java backend services (mdapdata-api)", 5)
    ]

    total_scripts = len(scripts_to_run)
    completed = 0
    
    for script, message, latency in scripts_to_run:
        print(f"\n[PROGRESS] {completed}/{total_scripts} scripts completed")
        
        script_path = os.path.abspath(script)
        
        if not os.path.exists(script_path):
            print(f"[ERROR] Script not found: {script_path}")
            continue
            
        success = run_script(script_path, message, latency)
        
        if success:
            completed += 1
        else:
            print("\n[ERROR] Script execution sequence stopped due to error")
            print(f"[STATUS] Completed {completed}/{total_scripts} scripts")
            return 1

    if completed == total_scripts:
        print("\n[SUCCESS] All scripts completed successfully!")
        print(f"[FINAL] Completed {completed}/{total_scripts} scripts")
        return 0
    
    return 1

if __name__ == "__main__":
    sys.exit(main())
