/* Suggestions: 1. Write a function for every command
                2. Start with the pwd command
                3. Continue with the other commands that do not have parameters
*/

mod pwd;
mod mkdir;
mod cat;
mod mv;
mod ln;
mod echo;
mod rmdir;
mod cp;

use pwd::pwd;
use echo::echo;
use mkdir::mkdir;
use cat::cat;
use mv::mv;
use ln::ln;
use rmdir::rmdir;
use cp::cp;
use std::env;
use std::process::ExitCode;

fn main() -> ExitCode {
    let args: Vec<String> = env::args().collect();

    if args.len() < 2 {
        eprintln!("Invalid command");
        return ExitCode::from(255);
    }

    let command = &args[1];

    let output: u8 = match command.as_str() {
        "pwd" => pwd(),
        "echo" => echo(&args),
        "mkdir" => mkdir(&args),
        "rmdir" => rmdir(&args),
        "cat" => cat(&args),
        "mv" => mv(&args),
        "ln" => ln(&args),
        "cp" => cp(&args),
        &_ => 255,
    };

    if output != 0 {
        eprintln!("Invalid command");
    }
    ExitCode::from(output)
}