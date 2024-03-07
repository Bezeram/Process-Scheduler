use std::fs;
use std::path::Path;

/**
 * Function has two main bodies: recursive and nonrecursive
 * Recursive version is used for copying directories
 */
fn cp_dir(src: &Path, dst: &Path) -> i8 {
    if src.is_dir() {
        if !dst.exists() {
            if let Err(_) = fs::create_dir(dst) {
                return -1;
            }
        }

        let dir_data_result = fs::read_dir(src);
        let dir_data = match dir_data_result {
            Ok(dir_data) => dir_data,
            Err(_) => return -1,
        };

        for dir_attribute_result in dir_data {
            let dir_attribute = match dir_attribute_result {
                Ok(dir_attribute) => dir_attribute,
                Err(_) => return -1,
            };

            if cp_dir(&dir_attribute.path(), &dst.join(dir_attribute.file_name())) == -1 {
                return -1;
            }
        }
    }
    else if src.is_file() {
        if let Err(_) = fs::copy(src, dst) {
            return -1;
        };
    }

    0
}

pub fn cp(args: &Vec<String>) -> u8 {
    const ERR_PARAM: u8 = 255;
    const ERR_CP: u8 = 166;

    if args.len() < 4 {
        return ERR_PARAM;
    }

    if args[2] == "-R" || args[2] == "-r" || args[2] == "-recursive" {
        let src = &args[3];
        let dst = &args[4];

        if cp_dir(Path::new(src), Path::new(dst)) == -1 {
            return ERR_CP;
        }
    } else {
        let src = &args[2];
        let dst = &args[3];
        let new_dst = if dst.ends_with('/') {
            dst.to_owned() + src
        }
        else {
            dst.to_owned() + "/" + src
        };

        let dst_path = if Path::is_dir(Path::new(dst)) {
            Path::new(&new_dst)
        }
        else {
            Path::new(dst)
        };

        if let Err(_) = fs::copy(Path::new(src), dst_path) {
            return ERR_CP;
        }
    }

    0
}