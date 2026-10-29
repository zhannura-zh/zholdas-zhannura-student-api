#!/bin/sh

LOG_FILE="logs/Zholdas_Zhannura_system_report.log"

{
  echo "=========================================="
  echo "System Monitoring Report"
  echo "Student Full Name: Zholdas Zhannura"
  echo "Date and Time    : $(date '+%Y-%m-%d %H:%M:%S')"
  echo "Current Username : $(whoami)"
  echo "OS & Kernel      : $(uname -s -r -v)"
  echo "------------------------------------------"
  echo "CPU Information:"
  cat /proc/cpuinfo | grep "model name" | head -n 1
  echo "------------------------------------------"
  echo "Memory Information:"
  free -h 2>/dev/null || free -m
  echo "------------------------------------------"
  echo "Disk Usage:"
  df -h /
  echo "------------------------------------------"
  echo "Available Disk Space: $(df -h / | awk 'NR==2 {print $4}')"
  echo "=========================================="
} | tee "$LOG_FILE"
