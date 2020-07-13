# CircleCI Server Keyset CLI

`server-keysets` is a CLI tool to generate new encryption and signing keysets for use in CircleCI Server installs.

## Usage:
`server-keysets <command> <command-arguments>`

Commands:
* `generate` - generate a new keyset.
  Arguments:
     * `encryption` - generate a keyset for encryption.
     * `signing` - generate a keyset for signing/signature verification.

Examples:
  * `server-keysets generate encryption`
  * `server-keysets generate signing`

## Building

This CLI tool is built using [GraalVM's](https://www.graalvm.org/) `native-image` compiler.

### Prerequisites
* [Install GraalVM](https://www.graalvm.org/getting-started/). There is a
  non-official [Homebrew cask](https://github.com/DeLaGuardo/homebrew-graalvm)
  available for macOS (at the time of writing this cask was confirmed to be a
  graalvm installer by inspection, it is worth confirming before installing).
* Install the [native-image](https://www.graalvm.org/getting-started/#native-images) plugin
* You will also need a compiler toolchain if you do not have one already
  installed. XCode for macOS should be sufficient, similary `gcc` for Linux.

### Compiling the binary
1. `lein clean`
1. `lein uberjar`
1. `lein generate-assisted-configuration` - due to the highly dynamic nature of
   Clojure we need to provide [`native-image` configuration
   files](https://github.com/oracle/graal/blob/master/substratevm/CONFIGURE.md)
   to provide data about uses of reflection, dynamic proxies, JNI etc. This
   script runs the program with a JVMTI agent attached that records these uses,
   generates the necessary configuration files, and adds them to the uberjar.
1. `lein native` - invokes `native-image` to build the binary
