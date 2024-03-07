/**
 * Function uses the command arguments and prints the ones after the command name.
 * If the option -n is entered, the newline is skipped at the end
 */
pub fn echo(args: &Vec<String>) -> u8 {
    const ERR_PARAM: u8 = 255;

    if args.len() < 3 {
        return ERR_PARAM;
    }

    let option = &args[2];
    let (newline, start_args) = 
    if option == "-n" {
        // Skip new line
        if args.len() == 3 {
            return ERR_PARAM;
        }

        (false, 3)
    } else {
        (true, 2)
    };

    let mut output = String::new();
    for i in start_args..args.len() {
        output.push_str(&args[i]);
        if i != args.len() - 1 {
            output.push(' ');
        }
    }

    if newline {
        output.push('\n');
    }

    print!("{}", output);
    0
}