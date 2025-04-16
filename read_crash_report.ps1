param (
    [string]$ReportPath = "run\crash-reports"
)

# Get the most recent crash report
$crashReports = Get-ChildItem -Path $ReportPath -Filter "crash-*.txt" | Sort-Object LastWriteTime -Descending
if ($crashReports.Count -eq 0) {
    Write-Host "No crash reports found in $ReportPath"
    exit 1
}

$latestCrash = $crashReports[0]
Write-Host "Reading latest crash report: $($latestCrash.Name) (Created: $($latestCrash.LastWriteTime))"
Write-Host "----------------------------------------"
Write-Host ""

# Read and display the crash report content with proper formatting
$content = Get-Content -Path $latestCrash.FullName
$description = $content | Where-Object { $_ -match "Description:" }
$causedBy = $content | Where-Object { $_ -match "Caused by:" }

Write-Host "CRASH SUMMARY:"
Write-Host "-------------"
if ($description) { Write-Host $description }
Write-Host ""
Write-Host "ROOT CAUSES:"
Write-Host "-------------"
foreach ($cause in $causedBy) {
    Write-Host $cause
}

Write-Host ""
Write-Host "FULL CRASH REPORT:"
Write-Host "-------------"
foreach ($line in $content) {
    Write-Host $line
}

Write-Host ""
Write-Host "----------------------------------------"
Write-Host "End of crash report: $($latestCrash.Name)"
