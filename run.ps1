# Define the source directory, resources directory, bin directory, and test directory
$SRC_DIR = "src"
$RES_DIR = "res"
$BIN_DIR = "bin"
$TEST_DIR = "src/test"

# Delete all .class files in the bin directory
Get-ChildItem -Path ./$BIN_DIR -Recurse -Include *.class | Remove-Item
Write-Output "Delete success"

# Create the bin directory if it doesn't exist
if (!(Test-Path -Path $BIN_DIR)) {
    New-Item -ItemType Directory -Path $BIN_DIR
}

$javaFiles = @(Get-ChildItem -Path $SRC_DIR -Recurse -Filter *.java | Where-Object { $_.DirectoryName -notmatch $TEST_DIR })
Write-Output "Number of Java files: $($javaFiles.Count)"
# Compile all Java files in the source directory (excluding the test directory) into the bin directory
Write-Output "Compiling Java file $($javaFiles[0].FullName)"
$compileOutput = $javaFiles | ForEach-Object {
    Write-Output "Compiling"
    try {
        $output = javac -cp $SRC_DIR -d $BIN_DIR $_.FullName 2>&1
        Write-Output "javac output: $output"
    } catch {
        Write-Output "Error: $_"
    }
}

# If compilation was successful, run the main class
if ($LASTEXITCODE -eq 0) {
    java -cp "$BIN_DIR;$RES_DIR" view.main.App
} else {
    Write-Output "Compilation failed"
}