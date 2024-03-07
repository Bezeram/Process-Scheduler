use std::fs;

/**
 * Function receives all command line arguments and checks if command can be attempted
 * Uses the rename() utility from standard file system
 */
pub fn mv(args: &Vec<String>) -> u8 {
    const ERR_MV: u8 = 216;
    const ERR_PARAM: u8 = 255;

    if args.len() < 4 {
        return ERR_PARAM;
    }

    let src = &args[2];
    let dest = &args[3];
    if let Err(_) = fs::rename(src, dest) {
        return ERR_MV;
    }

    0
}