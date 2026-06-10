export const tryCatch = (tryer: any): { result?: any; error?: any } => {
  try {
    const result = tryer();
    return { result };
  } catch (error) {
    return { error };
  }
};
