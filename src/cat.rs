use std::fs;

/**
 * Function takes all command line arguments and verifies them
 * Using the read_to_string() utility from standard file system, the contents can be accessed 
without calling fs::open()
 */
pub fn cat(args: &Vec<String>) -> u8 {
    const ERR_CAT: u8 = 206;
    const ERR_PARAM: u8 = 255;

    if args.len() < 3 {
        return ERR_PARAM;
    }

    let files = &args[2..].to_vec();

    for file_path in files {
        let read = fs::read_to_string(&file_path);

        match read {
            Ok(text) => {
                print!("{}", text);
            }
            Err(_) => return ERR_CAT,
        }
    }

    0
}