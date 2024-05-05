# Define the source directory, resources directory, bin directory, and test directory
$SRC_DIR = "src"
$RES_DIR = "res"
$BIN_DIR = "bin"
$TEST_DIR = "src/test"

# Delete all .class files in the bin directory
Get-ChildItem -Path $BIN_DIR -Recurse -Include *.class | Remove-Item -Force -ErrorAction SilentlyContinue
Write-Output "Deleted .class files in $BIN_DIR directory"

# Create the bin directory if it doesn't exist
if (!(Test-Path -Path $BIN_DIR)) {
    New-Item -ItemType Directory -Path $BIN_DIR | Out-Null
    Write-Output "Created $BIN_DIR directory"
}

# Get Java files in the source directory (excluding the test directory)
$javaFiles = Get-ChildItem -Path $SRC_DIR -Recurse -Filter *.java | Where-Object { $_.DirectoryName -notmatch $TEST_DIR }

# Compile Java files into the bin directory
Write-Output "Number of Java files: $($javaFiles.Count)"
foreach ($javaFile in $javaFiles) {
    Write-Output "Compiling $($javaFile.FullName)"
    try {
        $output = javac -cp $SRC_DIR -d $BIN_DIR $javaFile.FullName 2>&1
        Write-Output "javac output: $output"
    } catch {
        Write-Output "Error: $_"
    }
}

# If compilation was successful, run the main class
if ($?) {
    Write-Output "Compilation successful. Running the main class."
    java -cp "$BIN_DIR;$RES_DIR" view.main.App
} else {
    Write-Output "Compilation failed"
}