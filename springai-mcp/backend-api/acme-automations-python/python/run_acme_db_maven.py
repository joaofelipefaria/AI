import subprocess
import os
import sys
import shutil
from datetime import datetime

# Define a flag for emoji support
USE_EMOJI = False  # Set to False for Windows compatibility

# Define emoji alternatives
EMOJI = {
    'rocket': '🚀' if USE_EMOJI else '>',
    'clock': '⏰' if USE_EMOJI else '[TIME]',
    'search': '🔍' if USE_EMOJI else '[SEARCH]',
    'check': '✅' if USE_EMOJI else '[OK]',
    'folder': '📂' if USE_EMOJI else '[DIR]',
    'pin': '📍' if USE_EMOJI else '[PATH]',
    'list': '📋' if USE_EMOJI else '[STEP]',
    'gear': '⚙️' if USE_EMOJI else '[RUN]',
    'info': 'ℹ️' if USE_EMOJI else '[INFO]',
    'warning': '⚠️' if USE_EMOJI else '[WARN]',
    'error': '❌' if USE_EMOJI else '[ERROR]',
    'success': '✨' if USE_EMOJI else '[SUCCESS]'
}

def find_mvn():
    print(f"\n{EMOJI['search']} Locating Maven installation...")
    
    # First try your specific Maven installation
    custom_maven = r"E:\dev\tools\maven\apache-maven-3.9.6\bin\mvn.cmd"
    if os.path.exists(custom_maven):
        print(f"{EMOJI['check']} Found custom Maven installation at: {custom_maven}")
        return custom_maven
    
    # Fallback to PATH and other common locations
    mvn_cmd = shutil.which('mvn')
    if mvn_cmd:
        print(f"{EMOJI['check']} Found Maven in system PATH: {mvn_cmd}")
        return mvn_cmd
    
    # Common Maven installation locations on Windows
    possible_locations = [
        os.path.join(os.environ.get('MAVEN_HOME', ''), 'bin', 'mvn.cmd'),
        os.path.join(os.environ.get('M2_HOME', ''), 'bin', 'mvn.cmd'),
        r"C:\Program Files\Apache Maven\bin\mvn.cmd",
        r"C:\Program Files (x86)\Apache Maven\bin\mvn.cmd"
    ]
    
    for location in possible_locations:
        if os.path.exists(location):
            print(f"{EMOJI['check']} Found Maven at: {location}")
            return location
            
    raise FileNotFoundError(f"{EMOJI['error']} Maven executable not found. Please ensure Maven is installed and MAVEN_HOME or M2_HOME is set correctly.")

def run_maven_command():
    print(f"\n{EMOJI['rocket']} Starting Database Creation Process")
    print(f"{EMOJI['clock']} Start Time: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")
    print("=" * 50)

    try:
        mvn_path = find_mvn()

        project_dir = os.path.join(os.path.dirname(__file__), '..', '..', 'acme-dbadmin')
        print(f"\n{EMOJI['folder']} Project Directory: {project_dir}")
        
        if not os.path.exists(project_dir):
            raise FileNotFoundError(f"{EMOJI['error']} Project directory not found: {project_dir}")

        os.chdir(project_dir)
        print(f"{EMOJI['pin']} Working Directory: {os.getcwd()}")
        
        maven_commands = [
            {"cmd": [mvn_path, 'clean'], "desc": "Cleaning previous build files"},
            {"cmd": [mvn_path, 'compile'], "desc": "Compiling project"},
            {"cmd": [mvn_path, 'exec:java'], "desc": "Executing database creation"}
        ]

        for step, command in enumerate(maven_commands, 1):
            print(f"\n{EMOJI['list']} Step {step}/{len(maven_commands)}: {command['desc']}")
            print(f"{EMOJI['gear']} Executing: {' '.join(command['cmd'])}")
            print("-" * 50)
            
            try:
                result = subprocess.run(
                    command['cmd'],
                    check=True,
                    stdout=subprocess.PIPE,
                    stderr=subprocess.STDOUT,
                    universal_newlines=True,
                    encoding='utf-8'
                )
                
                output_lines = result.stdout.split('\n')
                relevant_output = [
                    line for line in output_lines 
                    if ('ERROR' in line or 'WARNING' in line or 
                        'INFO' in line or 'SUCCESS' in line or 
                        'Database' in line or 'Table' in line)
                ]
                
                if relevant_output:
                    print("\nRelevant Output:")
                    for line in relevant_output:
                        if 'ERROR' in line:
                            print(f"{EMOJI['error']} {line}")
                        elif 'WARNING' in line:
                            print(f"{EMOJI['warning']} {line}")
                        elif 'SUCCESS' in line or 'BUILD SUCCESS' in line:
                            print(f"{EMOJI['check']} {line}")
                        else:
                            print(f"{EMOJI['info']} {line}")
                
                print(f"\n{EMOJI['check']} {command['desc']} completed successfully")
                
            except subprocess.CalledProcessError as e:
                print(f"\n{EMOJI['error']} Maven Command Failed")
                print("Error Output:")
                print(e.output)
                raise e

        print(f"\n{EMOJI['success']} Database Creation Process Completed Successfully")
        print(f"{EMOJI['clock']} End Time: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")
        return 0

    except FileNotFoundError as e:
        print(f"\n{EMOJI['error']} Environment Error: {e}")
        print("\nSolution:")
        print("1. Verify Maven installation")
        print("2. Check environment variables (MAVEN_HOME or M2_HOME)")
        print("3. Ensure Maven is added to system PATH")
        return 1
        
    except subprocess.CalledProcessError as e:
        print(f"\n{EMOJI['error']} Maven Process Failed (Exit Code: {e.returncode})")
        return e.returncode
        
    except Exception as e:
        print(f"\n{EMOJI['error']} Unexpected Error: {str(e)}")
        print("\nPlease check:")
        print("1. Project structure and files")
        print("2. Database connection settings")
        print("3. System permissions")
        return 1

if __name__ == "__main__":
    sys.exit(run_maven_command())
