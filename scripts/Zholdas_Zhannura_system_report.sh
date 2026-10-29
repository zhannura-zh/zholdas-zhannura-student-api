#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")/.." && pwd)"
mkdir -p "$ROOT/logs"
LOG_FILE="$ROOT/logs/Zholdas_Zhannura_system_report.log"
{
    echo 'System Monitoring Report'
    echo 'Student Full Name: Zholdas Zhannura'
    printf 'Date and Time: '; date --iso-8601=seconds
    printf 'Current Username: '; whoami
    printf 'OS and Kernel: '; uname -a
    if [[ -r /etc/os-release ]]; then cat /etc/os-release; fi
    echo 'CPU Information:'
    awk -F ': ' '/model name/ { print $2; exit }' /proc/cpuinfo
    echo 'Memory Information:'
    free -h
    echo 'Disk Usage:'
    df -h "$ROOT"
    printf 'Available Disk Space: '
    df -h "$ROOT" | awk 'NR == 2 { print $4 }'
} | tee "$LOG_FILE"
