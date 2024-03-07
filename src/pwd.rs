use std::env;

/**
 * This function uses the env module to get the current directory from the process environment, by printing the working directory
 * Function should not return errors
 */
pub fn pwd() -> u8 {
    if let Ok(work_dir) = env::current_dir() {
        if let Some(curr_path) = work_dir.to_str() {
            println!("{}", curr_path);
        }
    }

    0
}