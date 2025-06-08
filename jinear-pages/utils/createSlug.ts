export const createSlug = ({text, maxWords = 12, maxLength = 255}: {
    text: string,
    maxWords?: number,
    maxLength?: number
}): string => {
    let processedText = text.toLowerCase().trim();

    // Limit to first n words
    processedText = processedText
        .split(/\s+/)
        .slice(0, maxWords)
        .join(' ');

    // Create initial slug
    let slug = processedText
        .replace(/[^\w\s-]/g, '')        // Remove special characters except word chars, spaces, and hyphens
        .replace(/[\s_-]+/g, '-')        // Replace spaces, underscores, and multiple hyphens with single hyphen
        .replace(/^-+|-+$/g, '');        // Remove leading and trailing hyphens

    // Trim to max length without breaking words
    if (slug.length > maxLength) {
        let trimmed = slug.substring(0, maxLength);
        let lastDash = trimmed.lastIndexOf('-');

        // If there's a dash within reasonable distance, cut there
        if (lastDash > maxLength * 0.8) {
            slug = trimmed.substring(0, lastDash);
        } else {
            // Otherwise find the last complete word
            slug = trimmed;
        }

        // Clean up any trailing hyphens
        slug = slug.replace(/-+$/, '');
    }

    return slug;
}

export default createSlug;
