import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { useDeleteByVenueMutation } from '../../../core/api/ticket';

type TicketVenueType = 'THEATRE' | 'CINEMA' | 'MALL' | 'STADIUM';

const keys: { value: string; type: 'text' | 'number' }[] = [
    { value: 'venueName', type: 'text' },
    { value: 'venueCapacity', type: 'number' },
    { value: 'venueType', type: 'text' },
];

interface IDataType {
    venueCapacity: number;
    venueName: string;
    venueType: TicketVenueType;
}

export const DeleteTicketByVenueForm: React.FC = () => {
    const { register, handleSubmit } = useForm();
    const [deleteByVenue] = useDeleteByVenueMutation();
    const [error, setError] = useState<string>('');
    const handleDeleteByVenue = async (data: IDataType) => {
        await deleteByVenue({
            name: data.venueName,
            type: data.venueType,
            capacity: data.venueCapacity,
        })
            .unwrap()
            .then(() => setError(''))
            .catch(() => setError('server error'));
    };

    return (
        <form
            className={'grid h-min w-60 grid-flow-row gap-2 rounded-2xl bg-light_purple p-5'}
            onSubmit={handleSubmit(handleDeleteByVenue)}
        >
            {keys.map((x) => {
                return (
                    <input
                        className={'h-[26px] w-full rounded border bg-light_purple pl-2 placeholder-gray'}
                        key={x.value}
                        {...register(x.value)}
                        type={x.type}
                        placeholder={x.value}
                    />
                );
            })}
            <button type={'submit'} className={'w-46 mt-2 h-[26px] rounded bg-purple'}>
                Get Venue Amount
            </button>
            <div className={'flex h-[30px] items-center justify-center text-red'}>{error}</div>
        </form>
    );
};
