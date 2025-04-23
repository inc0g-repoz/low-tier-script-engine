using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace lix4j_boot {

    class Program {

        static void Main(string[] args) {
            var processInfo = new ProcessStartInfo() {
                FileName = "java.exe",
                Arguments = "-jar lix4j.jar " + string.Join(" ", args),
                UseShellExecute = false,
                RedirectStandardOutput = true,
                CreateNoWindow = true
            };

            Process proc;
            if ((proc = Process.Start(processInfo)) == null) {
                throw new InvalidOperationException("??");
            }

            while (!proc.StandardOutput.EndOfStream) {
                string line = proc.StandardOutput.ReadLine();
                Console.WriteLine(line);
            }

            proc.WaitForExit();
            int exitCode = proc.ExitCode;
            proc.Close();
        }

    }

}
