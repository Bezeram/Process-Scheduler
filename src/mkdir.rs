use std::fs;

/**
 * Function receives all command line arguments and checks if command can be attempted
 * Uses the create_dir_all() utility from standard file system
 */
pub fn mkdir(args: &Vec<String>) -> u8 {
    const ERR_MKDIR: u8 = 226;
    const ERR_PARAM: u8 = 255;

    if args.len() < 3 {
        return ERR_PARAM;
    }

    // Extract the directories
    let dir_vec = &args[2..].to_vec();

    for dir in dir_vec {
        if let Err(_) = fs::create_dir_all(dir) {
            return ERR_MKDIR;
        }
    }

    0
}