# Security & Privacy Policy 🛡️

## 🔒 The Zero-Network Guarantee

Tinta is engineered with an uncompromising privacy-first philosophy:
* **Zero Network Permissions**: The `android.permission.INTERNET` permission is completely absent from `AndroidManifest.xml`. By architectural guarantee, the application cannot transmit any of your files, reading habits, or metadata over the network.
* **Local Sandboxing**: All recent document metadata is retained solely on your device within encrypted/sandboxed Android Jetpack DataStore preferences.
* **Explicit Storage Access**: Tinta only accesses files you explicitly pick via Android's native Storage Access Framework (SAF). It never scans or traverses your device's external storage without your direct interaction.

---

## 🚨 Reporting a Vulnerability

If you discover a security flaw or privacy concern in Tinta (e.g., unintended data leakage via local IPC, unsafe URI handling, or file injection):

1. **Do not open a public issue.**
2. Send an email directly to the maintainer: **mzhafraan** (or open a confidential security advisory through GitHub Security tab).
3. Include the following in your report:
   - Detailed description of the issue.
   - Steps to reproduce (including sample `.md` payload if applicable).
   - Expected vs actual behavior.
   - Android OS version and device architecture.

We will acknowledge receipt within 48 hours and work on an expedited resolution.
