use std::fs;
use std::path::Path;

/**
 * Function receives all command line arguments and checks if command can be attempted
 * Uses the remove_dir_all() utility from standard file system 
 */
pub fn rmdir(args: &Vec<String>) -> u8 {
    const ERR_RMDIR: u8 = 196;
    const ERR_PARAM: u8 = 255;

    if args.len() < 3 {
        return ERR_PARAM;
    }

    // Extract the directories
    let dir_vec = &args[2..].to_vec();

    for dir in dir_vec {
        let path_dir = Path::new(dir);
        match fs::read_dir(path_dir) {
            Ok(dir_data) => {
                if dir_data.count() != 0 {
                    return ERR_RMDIR;
                }
            }
            Err(_) => {
                return ERR_RMDIR;
            }
        }

        if let Err(_) = fs::remove_dir_all(dir) {
            return ERR_RMDIR;
        }
    }

    0
}