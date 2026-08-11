# API Limitations (Honesty System)

The following limitations must be strictly respected in the Android native implementation:

- **Real FPS of another game:** CANNOT be implemented (restricted by Android sandbox).
- **GPU utilization of another game:** CANNOT be implemented easily without root.
- **CPU/GPU frequency control:** CANNOT be implemented (requires root/custom kernel).
- **Automatic modification of another game's graphics:** CANNOT be implemented natively without root or an official game API.
- **Samsung Game Booster integration:** CANNOT be bypassed. Must refer users to Samsung system components.
- **Background game optimization:** MAY be implemented using standard Android Game Mode APIs where supported.
