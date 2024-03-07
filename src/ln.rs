use std::fs;
use std::os;

pub fn ln(args: &Vec<String>) -> u8 {
    const ERR_LN: u8 = 206;
    const ERR_PARAM: u8 = 255;

    if args.len() < 3 {
        return ERR_PARAM;
    }

    if args[2] == "-s" || args[2] == "-symbolic" {
        if let Err(_) = os::unix::fs::symlink(&args[3], &args[4]) {
            return ERR_LN;
        }
    } else {
        if let Err(_) = fs::hard_link(&args[2], &args[3]) {
            return ERR_LN;
        }
    };

    0
}