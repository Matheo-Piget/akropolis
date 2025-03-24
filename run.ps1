# Define the source directory, resources directory, bin directory, and test directory
$SRC_DIR = "src"
$RES_DIR = "res"
$BIN_DIR = "bin"
$TEST_DIR = "src\test"

# Create the bin directory if it doesn't exist
if (!(Test-Path -Path $BIN_DIR)) {
    New-Item -ItemType Directory -Path $BIN_DIR
    Write-Output "Created bin directory"
} else {
    # Delete all .class files in the bin directory
    Get-ChildItem -Path ./$BIN_DIR -Recurse -Include *.class | Remove-Item -ErrorAction SilentlyContinue
    Write-Output "Cleaned bin directory"
}

# Get all Java files in the source directory (COMPLETELY excluding test directory)
$javaFiles = @(Get-ChildItem -Path $SRC_DIR -Recurse -Filter *.java | 
               Where-Object { $_.FullName -notlike "*\test\*" })
Write-Output "Number of Java files to compile: $($javaFiles.Count)"

# Compile all Java files in one go with better classpath handling
Write-Output "Compiling Java files..."
try {
    # Execute javac with proper classpath settings
    javac -encoding UTF-8 -d $BIN_DIR -cp "$SRC_DIR" $javaFiles.FullName
    
    if ($LASTEXITCODE -eq 0 -or $null -eq $LASTEXITCODE) {
        Write-Output "Compilation successful!"
        Write-Output "Running application..."
        java -cp "$BIN_DIR;$RES_DIR" view.main.App
    } else {
        Write-Output "Compilation failed with exit code: $LASTEXITCODE"
        Write-Output "Check the error messages above for details."
    }
} catch {
    Write-Output "Error during compilation: $_"
}